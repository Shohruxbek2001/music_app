package uz.scala.routes

import cats.MonadThrow
import cats.implicits.catsSyntaxOptionId
import cats.implicits.toFlatMapOps
import io.estatico.newtype.ops.toCoercibleIdOps
import org.http4s.AuthedRoutes
import org.http4s.circe.JsonDecoder
import org.typelevel.log4cats.Logger

import uz.scala.Language
import uz.scala.ObjectId
import uz.scala.SuccessResult
import uz.scala.algebras.ProductsAlgebra
import uz.scala.algebras.SpecificationsAlgebra
import uz.scala.domain.AuthedUser
import uz.scala.domain.ProductId
import uz.scala.domain.SpecificationId
import uz.scala.domain.enums.Privilege
import uz.scala.domain.products.ProductInput
import uz.scala.domain.products.ProductSpecificationInput
import uz.scala.domain.products.ProductUpdateInput
import uz.scala.domain.users.Role
import uz.scala.http4s.syntax.all.deriveEntityEncoder
import uz.scala.http4s.syntax.all.http4SyntaxReqOps
import uz.scala.http4s.utils.Routes
import uz.scala.shared.ResponseMessages._
import uz.scala.syntax.all.coercibleEncoder

final case class ProductsRoutes[F[_]: Logger: JsonDecoder: MonadThrow](
    products: ProductsAlgebra[F],
    specifications: SpecificationsAlgebra[F],
  ) extends Routes[F, AuthedUser] {
  override val path = "/products"

  override val `private`: AuthedRoutes[AuthedUser, F] = AuthedRoutes.of {
    case ar @ POST -> Root as user =>
      implicit val language: Language = ar.req.lang
      implicit val role: Role = user.role
      authorize[F](Privilege.CreateProduct) {
        ar.req.decodeR[ProductInput] { input =>
          products
            .create(input)
            .flatMap(id => Created(ObjectId(id, PRODUCT_CREATED(language).some)))
        }
      }

    case ar @ PUT -> Root / UUIDVar(id) as user =>
      implicit val language: Language = ar.req.lang
      implicit val role: Role = user.role
      authorize[F](Privilege.UpdateProduct) {
        ar.req.decodeR[ProductUpdateInput] { input =>
          products
            .update(id.coerce[ProductId], input)
            .flatMap(_ => Ok(SuccessResult(PRODUCT_UPDATED(language))))
        }
      }

    case ar @ DELETE -> Root / UUIDVar(id) as user =>
      implicit val language: Language = ar.req.lang
      implicit val role: Role = user.role
      authorize[F](Privilege.DeleteProduct) {
        products
          .delete(id.coerce[ProductId])
          .flatMap(_ => Ok(SuccessResult(PRODUCT_DELETED(language))))
      }

    case ar @ POST -> Root / UUIDVar(id) / "specifications" as user =>
      implicit val language: Language = ar.req.lang
      implicit val role: Role = user.role
      authorize[F](Privilege.CreateProduct, Privilege.UpdateProduct) {
        ar.req.decodeR[ProductSpecificationInput] { input =>
          specifications
            .addToProduct(id.coerce[ProductId], input)
            .flatMap(_ => Ok(SuccessResult(PRODUCT_SPECIFICATION_ADDED(language))))
        }
      }

    case ar @ DELETE -> Root / UUIDVar(productId) / "specifications" / UUIDVar(specId) as user =>
      implicit val language: Language = ar.req.lang
      implicit val role: Role = user.role
      authorize[F](Privilege.UpdateProduct) {
        specifications
          .deleteFromProduct(productId.coerce[ProductId], specId.coerce[SpecificationId])
          .flatMap(_ => Ok(SuccessResult(PRODUCT_SPECIFICATION_DELETED(language))))
      }

  }
}
