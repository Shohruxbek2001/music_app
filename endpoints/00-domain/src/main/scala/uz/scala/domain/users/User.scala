package uz.scala.domain.users

import java.time.LocalDate
import java.time.ZonedDateTime

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._

import uz.scala.domain.Phone
import uz.scala.domain.UserId
import uz.scala.domain.enums.UserStatus
import uz.scala.domain.markets.MarketInfo
import uz.scala.syntax.circe._

@JsonCodec
case class User(
    id: UserId,
    createdAt: ZonedDateTime,
    firstname: NonEmptyString,
    lastname: NonEmptyString,
    status: UserStatus,
    phone: Phone,
    role: Role,
    birthday: Option[LocalDate],
    address: Option[NonEmptyString],
    market: Option[MarketInfo],
  )
