package uz.scala

import _root_.doobie.ConnectionIO
import _root_.doobie.Transactor
import cats.effect.Sync
import cats.effect.std.Random
import cats.~>
import org.typelevel.log4cats.Logger

import uz.scala.algebras._
import uz.scala.auth.AuthConfig
import uz.scala.auth.impl.Auth
import uz.scala.aws.s3.S3Client
import uz.scala.domain.AuthedUser
import uz.scala.redis.RedisClient

case class Algebras[F[_]](
    auth: Auth[F, AuthedUser],
    assets: AssetsAlgebra[F],
    users: UsersAlgebra[F],
    roles: RolesAlgebra[F],
    products: ProductsAlgebra[F],
    categories: CategoriesAlgebra[F],
    specifications: SpecificationsAlgebra[F],
    markets: MarketsAlgebra[F],
    orders: OrdersAlgebra[F],
  )

object Algebras {
  def make[F[_]: Sync: Logger: Random](
      s3Client: S3Client[F],
      config: AuthConfig,
      repositories: Repositories[ConnectionIO],
      redis: RedisClient[F],
    )(implicit
      xa: Transactor[F],
      lifter: F ~> ConnectionIO,
    ): Algebras[F] = {
    val users = UsersAlgebra.make[F](repositories.users, repositories.roles)
    val roles = RolesAlgebra.make[F](repositories.roles)
    Algebras[F](
      auth = Auth.make[F](config, users, repositories.roles, repositories.markets, redis),
      assets = AssetsAlgebra.make[F](s3Client),
      users = users,
      roles = roles,
      products = ProductsAlgebra
        .make[F](repositories.products, repositories.specifications),
      categories = CategoriesAlgebra.make[F](repositories.categories),
      specifications = SpecificationsAlgebra.make[F](
        repositories.specifications,
        repositories.categories,
      ),
      markets = MarketsAlgebra.make[F](repositories.markets),
      orders = OrdersAlgebra.make[F](repositories.orders),
    )
  }
}
