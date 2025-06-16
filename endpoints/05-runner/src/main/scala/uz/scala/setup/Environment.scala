package uz.scala.setup

import cats.effect.Async
import cats.effect.Resource
import cats.effect.std.Console
import cats.effect.std.Random
import cats.~>
import dev.profunktor.redis4cats.Redis
import dev.profunktor.redis4cats.effect.Log.NoOp.instance
import doobie.ConnectionIO
import doobie.WeakAsync
import doobie.syntax.connectionio.toConnectionIOOps
import eu.timepit.refined.pureconfig._
import fs2.concurrent.Topic
import org.http4s.server
import org.typelevel.log4cats.Logger
import pureconfig.generic.auto.exportReader
import pureconfig.module.cron4s._

import uz.scala.Algebras
import uz.scala.JobsEnvironment
import uz.scala.Repositories
import uz.scala.auth.impl.LiveMiddleware
import uz.scala.aws.s3.S3Client
import uz.scala.domain.AuthedUser
import uz.scala.domain.enums.Privilege
import uz.scala.domain.events.AppEvent
import uz.scala.doobie.DoobieTransaction
import uz.scala.flyway.Migrations
import uz.scala.http.{ Environment => ServerEnvironment }
import uz.scala.redis.RedisClient
import uz.scala.utils.ConfigLoader

case class Environment[F[_]: Async: Logger: Random](
    config: Config,
    repositories: Repositories[ConnectionIO],
    s3Client: S3Client[F],
    redis: RedisClient[F],
    middleware: server.AuthMiddleware[F, AuthedUser],
    appMiddleware: server.AuthMiddleware[F, Unit],
    appTopic: Topic[F, AppEvent],
    webTopic: Topic[F, AppEvent],
  )(implicit
    xa: doobie.Transactor[F],
    lifter: F ~> ConnectionIO,
  ) {
  private val algebras: Algebras[F] =
    Algebras.make[F](s3Client, config.auth, repositories, redis)
  lazy val toServer: ServerEnvironment[F] =
    ServerEnvironment(
      middleware = middleware,
      appMiddleware = appMiddleware,
      config = config.http,
      algebras = algebras,
      appTopic = appTopic,
      webTopic = webTopic,
    )

  lazy val toJobs: JobsEnvironment[F] = JobsEnvironment(
    repos = repositories,
    xa = xa,
  )
}
object Environment {
  def make[F[_]: Async: Console: Logger]: Resource[F, Environment[F]] =
    for {
      config <- Resource.eval(ConfigLoader.load[F, Config])
      _ <- Resource.eval(Migrations.run[F](config.migrations))
      implicit0(xa: doobie.Transactor[F]) = DoobieTransaction.make[F](config.database)
      repositories = Repositories.make
      redis <- Redis[F].utf8(config.redis.uri.toString).map(RedisClient[F](_, config.redis.prefix))
      _ <- Resource.eval(repositories.roles.insertPrivileges(Privilege.values.toList).transact(xa))
      middleware = LiveMiddleware.make[F](config.auth, redis)
      appMiddleware = LiveMiddleware.makeForApp[F](config.auth)
      implicit0(random: Random[F]) <- Resource.eval(Random.scalaUtilRandom[F])
      implicit0(lifter: (F ~> ConnectionIO)) <- WeakAsync.liftK[F, ConnectionIO]
      appTopic <- Resource.eval(Topic[F, AppEvent])
      webTopic <- Resource.eval(Topic[F, AppEvent])
      s3Client <- S3Client.resource(config.awsConfig)
    } yield Environment[F](
      config = config,
      repositories = repositories,
      s3Client = s3Client,
      redis = redis,
      middleware = middleware,
      appMiddleware = appMiddleware,
      appTopic = appTopic,
      webTopic = webTopic,
    )
}
