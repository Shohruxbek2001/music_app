package uz.scala.domain.products

import cats.data.NonEmptyList
import eu.timepit.refined.types.numeric.NonNegInt
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._
import squants.Money

import uz.scala.domain.CategoryId
import uz.scala.domain.MarketId
import uz.scala.syntax.circe._

@JsonCodec
case class ProductInput(
    name: NonEmptyString,
    price: Money,
    discountPrice: Option[Money],
    description: Option[NonEmptyString],
    categoryId: CategoryId,
    marketId: MarketId,
    quantity: Option[NonNegInt],
    specifications: Option[NonEmptyList[ProductSpecificationInput]],
  )
