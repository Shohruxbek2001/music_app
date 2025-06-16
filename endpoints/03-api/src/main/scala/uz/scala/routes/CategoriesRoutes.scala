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
import uz.scala.algebras.CategoriesAlgebra
import uz.scala.domain.AuthedUser
import uz.scala.domain.CategoryId
import uz.scala.domain.categories.CategoryInput
import uz.scala.domain.enums.Privilege
import uz.scala.domain.users.Role
import uz.scala.http4s.syntax.all.deriveEntityEncoder
import uz.scala.http4s.syntax.all.http4SyntaxReqOps
import uz.scala.http4s.utils.Routes
import uz.scala.shared.ResponseMessages.CATEGORY_CREATED
import uz.scala.shared.ResponseMessages.CATEGORY_DELETED
import uz.scala.shared.ResponseMessages.CATEGORY_UPDATED
import uz.scala.syntax.all.coercibleEncoder

final case class CategoriesRoutes[F[_]: Logger: JsonDecoder: MonadThrow](
    categories: CategoriesAlgebra[F]
  ) extends Routes[F, AuthedUser] {
  override val path = "/categories"

  override val `private`: AuthedRoutes[AuthedUser, F] = AuthedRoutes.of {
    case ar @ POST -> Root as user =>
      implicit val language: Language = ar.req.lang
      implicit val role: Role = user.role
      authorize[F](Privilege.CreateCategory) {
        ar.req.decodeR[CategoryInput] { input =>
          categories
            .create(input)
            .flatMap(id => Created(ObjectId(id, CATEGORY_CREATED(language).some)))
        }
      }
    case ar @ GET -> Root as user =>
      implicit val role: Role = user.role
      implicit val language: Language = ar.req.lang
      authorize[F](Privilege.ViewCategories) {
        categories.getAll.flatMap(Ok(_))
      }

    case ar @ PUT -> Root / UUIDVar(id) as user =>
      implicit val language: Language = ar.req.lang
      implicit val role: Role = user.role
      authorize[F](Privilege.UpdateCategory) {
        ar.req.decodeR[CategoryInput] { input =>
          categories
            .update(id.coerce[CategoryId], input)
            .flatMap(_ => Ok(SuccessResult(CATEGORY_UPDATED(language))))
        }
      }

    case ar @ DELETE -> Root / UUIDVar(id) as user =>
      implicit val language: Language = ar.req.lang
      implicit val role: Role = user.role
      authorize[F](Privilege.DeleteCategory) {
        categories
          .delete(id.coerce[CategoryId])
          .flatMap(_ => Ok(SuccessResult(CATEGORY_DELETED(language))))
      }
  }
}
