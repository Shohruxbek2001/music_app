package uz.scala.domain.products

import eu.timepit.refined.types.numeric.NonNegInt
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._

import uz.scala.domain.MarketId
import uz.scala.domain.ProductId
import uz.scala.syntax.circe._

@JsonCodec
case class ProductFilters(
    sku: Option[NonEmptyString] = None,
    name: Option[NonEmptyString] = None,
    barcode: Option[NonEmptyString] = None,
    categoryId: Option[String] = None,
    marketId: Option[MarketId] = None,
    productId: Option[ProductId] = None,
    limit: Option[NonNegInt] = None,
    page: Option[NonNegInt] = None,
  )
