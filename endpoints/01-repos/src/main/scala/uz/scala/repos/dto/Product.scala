package uz.scala.repos.dto

import java.time.ZonedDateTime

import eu.timepit.refined.types.numeric.NonNegInt
import eu.timepit.refined.types.string.NonEmptyString
import squants.Money

import uz.scala.domain.CategoryId
import uz.scala.domain.MarketId
import uz.scala.domain.ProductId

case class Product(
    id: ProductId,
    slug: NonEmptyString,
    name: NonEmptyString,
    description: Option[NonEmptyString],
    price: Money,
    discountPrice: Option[Money],
    stockQuantity: NonNegInt,
    categoryId: CategoryId,
    marketId: MarketId,
    createdAt: ZonedDateTime,
    updatedAt: Option[ZonedDateTime] = None,
    deletedAt: Option[ZonedDateTime] = None,
  )
