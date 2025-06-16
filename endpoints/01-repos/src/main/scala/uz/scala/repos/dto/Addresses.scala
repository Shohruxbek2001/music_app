package uz.scala.repos.dto

import java.time.ZonedDateTime

import eu.timepit.refined.types.string.NonEmptyString

import uz.scala.domain.AddressId
import uz.scala.domain.CustomerId

case class Addresses(
    id: AddressId,
    customerId: CustomerId,
    street: NonEmptyString,
    city: NonEmptyString,
    region: NonEmptyString,
    postalCode: Option[NonEmptyString],
    country: Option[NonEmptyString],
    createdAt: ZonedDateTime,
    updatedAt: Option[ZonedDateTime] = None,
    deletedAt: Option[ZonedDateTime] = None,
  )
