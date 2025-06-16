package uz.scala.domain.users

import java.time.LocalDate

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._

import uz.scala.domain.MarketId
import uz.scala.domain.Phone
import uz.scala.domain.RoleId
import uz.scala.domain.enums.UserStatus
import uz.scala.syntax.circe._

@JsonCodec
case class UpdateUserInput(
    firstname: Option[NonEmptyString],
    lastname: Option[NonEmptyString],
    phone: Option[Phone],
    status: Option[UserStatus],
    roleId: Option[RoleId],
    birthday: Option[LocalDate],
    address: Option[NonEmptyString],
    marketId: Option[MarketId],
  )
