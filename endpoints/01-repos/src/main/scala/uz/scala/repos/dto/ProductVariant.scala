package uz.scala.repos.dto

import eu.timepit.refined.types.numeric.NonNegInt
import eu.timepit.refined.types.string.NonEmptyString
import squants.Money
import uz.scala.domain.{CategoryId, ProductId, ProductVariantId}

import java.time.ZonedDateTime

case class ProductVariant(
    id: ProductVariantId,
    productId: ProductId,
    name: NonEmptyString,
    price: Money,
    discount_price: Option[Money],
    stockQuantity: NonNegInt,
    categoryId: CategoryId,
    createdAt: ZonedDateTime,
    updatedAt: Option[ZonedDateTime] = None,
    deletedAt: Option[ZonedDateTime] = None,
  )
