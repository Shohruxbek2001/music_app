package uz.scala.domain.users

import java.time.LocalDate

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._

import uz.scala.Language
import uz.scala.domain.MarketId
import uz.scala.domain.Phone
import uz.scala.domain.RoleId
import uz.scala.domain.enums.Privilege
import uz.scala.shared.ResponseMessages.CREATE_SUPER_USER
import uz.scala.shared.ResponseMessages.PRIVILEGE_CREATE_SUPER_USER
import uz.scala.shared.ResponseMessages.PRIVILEGE_CREATE_USER
import uz.scala.syntax.all.BooleanOps
import uz.scala.syntax.circe._
import uz.scala.validation.Rules
import uz.scala.validation.createRule

@JsonCodec
case class UserInput(
    firstname: NonEmptyString,
    lastname: NonEmptyString,
    phone: Phone,
    roleId: RoleId,
    birthday: Option[LocalDate] = None,
    address: Option[NonEmptyString] = None,
    marketId: Option[MarketId],
  )

object UserInput {
  implicit def validate(implicit userRole: Role, language: Language): Rules[Role] =
    List(
      createRule[Role](PRIVILEGE_CREATE_USER(language)) { role =>
        userRole.privileges.contains(Privilege.CreateUser)
      },
      createRule[Role](CREATE_SUPER_USER(language)) { role =>
        !role.privileges.contains(Privilege.CreateSuperUser)
      },
      createRule[Role](PRIVILEGE_CREATE_SUPER_USER(language)) { role =>
        val cond = role
          .privileges
          .exists(
            List(Privilege.ViewUsers, Privilege.CreateUser, Privilege.UpdateAnyUser).contains
          )
        Boolean.or(
          !cond,
          Boolean.and(
            cond,
            userRole.privileges.contains(Privilege.CreateSuperUser),
          ),
        )
      },
    )
}
