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
import uz.scala.algebras.SpecificationsAlgebra
import uz.scala.domain.AuthedUser
import uz.scala.domain.SpecificationId
import uz.scala.domain.SpecificationValueId
import uz.scala.domain.enums.Privilege
import uz.scala.domain.specifications.SpecificationFilters
import uz.scala.domain.specifications.SpecificationInput
import uz.scala.domain.specifications.SpecificationValueInput
import uz.scala.domain.specifications.UpdateSpecificationInput
import uz.scala.domain.users.Role
import uz.scala.http4s.syntax.all.deriveEntityEncoder
import uz.scala.http4s.syntax.all.http4SyntaxReqOps
import uz.scala.http4s.utils.Routes
import uz.scala.shared.ResponseMessages.SPECIFICATION_CREATED
import uz.scala.shared.ResponseMessages.SPECIFICATION_DELETED
import uz.scala.shared.ResponseMessages.SPECIFICATION_UPDATED
import uz.scala.shared.ResponseMessages.SPECIFICATION_VALUE_CREATED
import uz.scala.shared.ResponseMessages.SPECIFICATION_VALUE_DELETED
import uz.scala.shared.ResponseMessages.SPECIFICATION_VALUE_UPDATED
import uz.scala.syntax.all.coercibleEncoder

final case class SpecificationsRoutes[F[_]: Logger: JsonDecoder: MonadThrow](
    specifications: SpecificationsAlgebra[F]
  ) extends Routes[F, AuthedUser] {
  override val path = "/specifications"

  override val `private`: AuthedRoutes[AuthedUser, F] = AuthedRoutes.of {
    case ar @ POST -> Root as user =>
      implicit val language: Language = ar.req.lang
      implicit val role: Role = user.role
      authorize[F](Privilege.CreateSpecification) {
        ar.req.decodeR[SpecificationInput] { input =>
          specifications
            .create(input)
            .flatMap(id => Created(ObjectId(id, SPECIFICATION_CREATED(language).some)))
        }
      }

    case ar @ POST -> Root / "search" as user =>
      implicit val language: Language = ar.req.lang
      implicit val role: Role = user.role
      authorize[F](Privilege.ViewSpecifications) {
        ar.req.decodeR[SpecificationFilters] { filters =>
          specifications
            .get(filters)
            .flatMap(Ok(_))
        }
      }

    case ar @ PUT -> Root / UUIDVar(id) as user =>
      implicit val language: Language = ar.req.lang
      implicit val role: Role = user.role
      authorize[F](Privilege.UpdateSpecification) {
        ar.req.decodeR[UpdateSpecificationInput] { input =>
          specifications
            .update(id.coerce[SpecificationId], input)
            .flatMap(_ => Ok(SuccessResult(SPECIFICATION_UPDATED(language))))
        }
      }

    case ar @ DELETE -> Root / UUIDVar(id) as user =>
      implicit val language: Language = ar.req.lang
      implicit val role: Role = user.role
      authorize[F](Privilege.DeleteSpecification) {
        specifications
          .delete(id.coerce[SpecificationId])
          .flatMap(_ => Ok(SuccessResult(SPECIFICATION_DELETED(language))))
      }

    case ar @ POST -> Root / UUIDVar(specId) / "values" as user =>
      implicit val language: Language = ar.req.lang
      implicit val role: Role = user.role
      authorize[F](Privilege.CreateSpecification) {
        ar.req.decodeR[SpecificationValueInput] { input =>
          specifications
            .createValue(specId.coerce[SpecificationId], input.value)
            .flatMap(id => Created(ObjectId(id, SPECIFICATION_VALUE_CREATED(language).some)))
        }
      }

    case ar @ PUT -> Root / UUIDVar(specId) / "values" / UUIDVar(valueId) as user =>
      implicit val language: Language = ar.req.lang
      implicit val role: Role = user.role
      authorize[F](Privilege.UpdateSpecification) {
        ar.req.decodeR[SpecificationValueInput] { input =>
          specifications
            .updateValue(
              specId.coerce[SpecificationId],
              valueId.coerce[SpecificationValueId],
              input.value,
            )
            .flatMap(_ => Ok(SuccessResult(SPECIFICATION_VALUE_UPDATED(language))))
        }
      }

    case ar @ DELETE -> Root / UUIDVar(specId) / "values" / UUIDVar(valueId) as user =>
      implicit val language: Language = ar.req.lang
      implicit val role: Role = user.role
      authorize[F](Privilege.DeleteSpecification) {
        specifications
          .deleteValue(specId.coerce[SpecificationId], valueId.coerce[SpecificationValueId])
          .flatMap(_ => Ok(SuccessResult(SPECIFICATION_VALUE_DELETED(language))))
      }
  }
}
