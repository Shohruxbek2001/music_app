package uz.scala.domain

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._

import uz.scala.domain.enums.UserStatus
import uz.scala.domain.markets.MarketInfo
import uz.scala.domain.users.Role
import uz.scala.syntax.circe._

@JsonCodec
case class AuthedUser(
    id: UserId,
    role: Role,
    name: NonEmptyString,
    email: EmailAddress,
    status: UserStatus,
    phone: Option[Phone],
    market: Option[MarketInfo] = None,
  )
