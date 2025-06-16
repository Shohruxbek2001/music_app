package uz.scala.repos.dto

import java.time.ZonedDateTime

import eu.timepit.refined.types.numeric.NonNegInt
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Json
import squants.Money

import uz.scala.domain.CategoryId
import uz.scala.domain.ProductId

case class ProductDetails(
    id: ProductId,
    sku: NonEmptyString,
    slug: NonEmptyString,
    name: NonEmptyString,
    description: Option[NonEmptyString],
    price: Money,
    discount_price: Option[Money],
    stockQuantity: NonNegInt,
    categoryId: CategoryId,
    createdAt: ZonedDateTime,
    updatedAt: Option[ZonedDateTime] = None,
    deletedAt: Option[ZonedDateTime] = None,
    specifications: Json,
  )
