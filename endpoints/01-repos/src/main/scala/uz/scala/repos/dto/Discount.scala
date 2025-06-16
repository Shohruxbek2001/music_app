package uz.scala.repos.dto

import java.time.ZonedDateTime

import eu.timepit.refined.types.numeric.NonNegInt
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString

import uz.scala.domain.DiscountId
import uz.scala.domain.Percentage

case class Discount(
    id: DiscountId,
    code: NonEmptyString,
    description: Option[NonEmptyString],
    discountPercent: Percentage,
    validFrom: Option[ZonedDateTime],
    validUntil: Option[ZonedDateTime],
    maxUses: Option[PosInt],
    timesUsed: Option[NonNegInt],
  )
