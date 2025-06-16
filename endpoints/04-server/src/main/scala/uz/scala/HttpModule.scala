package uz.scala

import cats.data.NonEmptyList
import cats.effect.Async
import cats.effect.ExitCode
import cats.effect.kernel.Resource
import cats.implicits.toFunctorOps
import cats.implicits.toSemigroupKOps
import fs2.concurrent.Topic
import org.http4s.HttpRoutes
import org.http4s.circe.JsonDecoder
import org.http4s.server.Router
import org.http4s.server.websocket.WebSocketBuilder2
import org.typelevel.log4cats.Logger

import uz.scala.domain.AuthedUser
import uz.scala.domain.events.AppEvent
import uz.scala.http.Environment
import uz.scala.http4s.HttpServer
import uz.scala.http4s.utils.Routes
import uz.scala.routes._

object HttpModule {
  private def allRoutes[F[_]: Async: JsonDecoder: Logger](
      env: Environment[F]
    ): NonEmptyList[HttpRoutes[F]] = {
    implicit val webTopic: Topic[F, AppEvent] = env.webTopic
    NonEmptyList
      .of[Routes[F, AuthedUser]](
        new AuthRoutes[F](env.algebras.auth),
        new UsersRoutes[F](env.algebras.users, env.algebras.roles),
        new RolesRoutes[F](env.algebras.roles),
        new MarketRoutes[F](
          env.algebras.products,
          env.algebras.markets,
          env.algebras.orders,
        ),
        new ProductsRoutes[F](
          env.algebras.products,
          env.algebras.specifications,
        ),
        new CategoriesRoutes[F](env.algebras.categories),
        new SpecificationsRoutes[F](env.algebras.specifications),
        new RootRoutes[F](env.algebras.assets),
      )
      .map { r =>
        Router(
          r.path -> (r.public <+> env.middleware(r.`private`))
        )
      }
  }

  private def appRoutes[F[_]: Async: JsonDecoder: Logger](
      env: Environment[F]
    )(implicit
      wsb: WebSocketBuilder2[F]
    ): NonEmptyList[HttpRoutes[F]] = {
    implicit val webTopic: Topic[F, AppEvent] = env.webTopic
    NonEmptyList
      .of[Routes[F, Unit]](
        new AppRoutes[F](env.algebras.orders, env.appTopic)
      )
      .map { r =>
        Router(
          r.path -> (r.public <+> env.appMiddleware(r.`private`))
        )
      }
  }

  def make[F[_]: Async](
      env: Environment[F]
    )(implicit
      logger: Logger[F]
    ): Resource[F, F[ExitCode]] =
    HttpServer
      .make[F](env.config, implicit wsb => appRoutes[F](env) ::: allRoutes[F](env))
      .map { _ =>
        logger.info(s"HTTP server is started").as(ExitCode.Success)
      }
}
