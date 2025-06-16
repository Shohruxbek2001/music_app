package uz.scala.repos.dto

import java.time.ZonedDateTime

import eu.timepit.refined.types.string.NonEmptyString

import uz.scala.domain.CustomerId
import uz.scala.domain.ProductId
import uz.scala.domain.RatingId
import uz.scala.domain.RatingType

case class Rating(
    id: RatingId,
    customerId: CustomerId,
    productId: ProductId,
    rating: RatingType,
    comment: Option[NonEmptyString],
    createdAt: ZonedDateTime,
    updatedAt: Option[ZonedDateTime] = None,
    deletedAt: Option[ZonedDateTime] = None,
  )
