package uz.scala.repos.dto

import java.time.ZonedDateTime

import eu.timepit.refined.types.string.NonEmptyString
import tsec.passwordhashers.PasswordHash
import tsec.passwordhashers.jca.SCrypt

import uz.scala.domain._
import uz.scala.domain.enums.AuthProvider

case class Customers(
    id: UserId,
    email: EmailAddress,
    name: NonEmptyString,
    phone: Option[Phone],
    authProvider: AuthProvider,
    providerUserId: Option[NonEmptyString],
    password: PasswordHash[SCrypt],
    createdAt: ZonedDateTime,
    updatedAt: Option[ZonedDateTime] = None,
    deletedAt: Option[ZonedDateTime] = None,
  )
