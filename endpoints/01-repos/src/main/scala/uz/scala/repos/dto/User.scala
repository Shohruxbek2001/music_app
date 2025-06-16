package uz.scala.repos.dto

import java.time.ZonedDateTime

import eu.timepit.refined.types.string.NonEmptyString
import io.scalaland.chimney.dsl.TransformationOps
import tsec.passwordhashers.PasswordHash
import tsec.passwordhashers.jca.SCrypt

import uz.scala.domain.AuthedUser
import uz.scala.domain.EmailAddress
import uz.scala.domain.MarketId
import uz.scala.domain.Phone
import uz.scala.domain.RoleId
import uz.scala.domain.UserId
import uz.scala.domain.enums.UserStatus

case class User(
    id: UserId,
    createdAt: ZonedDateTime,
    name: NonEmptyString,
    email: EmailAddress,
    roleId: RoleId,
    status: UserStatus,
    phone: Option[Phone],
    password: PasswordHash[SCrypt],
    marketId: Option[MarketId] = None,
    updatedAt: Option[ZonedDateTime] = None,
    deletedAt: Option[ZonedDateTime] = None,
  ) {
  def toAuth(role: uz.scala.domain.users.Role, market: Option[MarketInfo]): AuthedUser =
    this
      .into[AuthedUser]
      .withFieldConst(_.role, role)
      .withFieldConst(_.market, market.map(_.toDomain))
      .transform
}
