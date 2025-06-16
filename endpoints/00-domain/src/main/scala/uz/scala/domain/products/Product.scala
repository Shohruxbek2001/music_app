package uz.scala.domain.products

import java.time.ZonedDateTime

import eu.timepit.refined.types.numeric.NonNegInt
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._

import uz.scala.domain.CategoryId
import uz.scala.domain.ProductId
import uz.scala.syntax.circe._

@JsonCodec
case class Product(
    id: ProductId,
    sku: Int,
    name: NonEmptyString,
    stockQuantity: NonNegInt,
    storeQuantity: NonNegInt,
    createdAt: ZonedDateTime,
    categoryId: CategoryId,
  )
