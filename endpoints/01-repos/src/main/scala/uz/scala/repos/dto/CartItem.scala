package uz.scala.repos.dto

import java.time.ZonedDateTime

import eu.timepit.refined.types.numeric.NonNegInt

import uz.scala.domain.CartItemId
import uz.scala.domain.CustomerId
import uz.scala.domain.ProductVariantId

case class CartItem(
    id: CartItemId,
    customerId: CustomerId,
    productVariantId: ProductVariantId,
    quantity: NonNegInt,
    addedAt: ZonedDateTime,
  )
