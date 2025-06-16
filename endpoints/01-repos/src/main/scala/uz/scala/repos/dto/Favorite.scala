package uz.scala.repos.dto

import java.time.ZonedDateTime

import eu.timepit.refined.types.numeric.NonNegInt

import uz.scala.domain.CustomerId
import uz.scala.domain.FavoriteId
import uz.scala.domain.ProductId

case class Favorite(
    id: FavoriteId,
    customerId: CustomerId,
    productId: ProductId,
    quantity: NonNegInt,
    addedAt: ZonedDateTime,
  )
