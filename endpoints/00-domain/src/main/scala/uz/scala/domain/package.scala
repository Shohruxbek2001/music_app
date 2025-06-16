package uz.scala

import java.util.UUID

import derevo.cats.eqv
import derevo.cats.show
import derevo.derive
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Interval.Closed
import eu.timepit.refined.string.MatchesRegex
import io.estatico.newtype.macros.newtype
import squants.market.Currency

import uz.scala.syntax.refined._
import uz.scala.utils.uuid

package object domain {
  // refined types
  type RatingType = Int Refined Closed[1, 5]
  type Phone = String Refined MatchesRegex[W.`"""[+][\\d]{12}+"""`.T]
  type EmailAddress =
    String Refined MatchesRegex[W.`"[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,}"`.T]
  type Percentage = BigDecimal Refined Closed[1, 100]
  object Percentage {
    def fromBigDecimal(value: BigDecimal): Percentage =
      value.setScale(2, BigDecimal.RoundingMode.HALF_UP) * 100
  }

  object UZS extends Currency("UZS", "Uzbek sum", "SUM", 2)

  // newtypes
  @derive(eqv, show, uuid)
  @newtype case class UserId(value: UUID)
  @derive(eqv, show, uuid)
  @newtype case class AssetId(value: UUID)
  @derive(eqv, show, uuid)
  @newtype case class RoleId(value: UUID)
  @derive(eqv, show, uuid)
  @newtype case class ProductId(value: UUID)
  @derive(eqv, show, uuid)
  @newtype case class ProductVariantId(value: UUID)
  @derive(eqv, show, uuid)
  @newtype case class ProductImageId(value: UUID)
  @derive(eqv, show, uuid)
  @newtype case class CategoryId(value: UUID)
  @derive(eqv, show, uuid)
  @newtype case class SpecificationId(value: UUID)
  @derive(eqv, show, uuid)
  @newtype case class SpecificationValueId(value: UUID)
  @derive(eqv, show, uuid)
  @newtype case class MarketId(value: UUID)
  @derive(eqv, show, uuid)
  @newtype case class OrderId(value: UUID)
  @derive(eqv, show, uuid)
  @newtype case class OrderItemId(value: UUID)
  @derive(eqv, show, uuid)
  @newtype case class CustomerId(value: UUID)
  @derive(eqv, show, uuid)
  @newtype case class AddressId(value: UUID)
  @derive(eqv, show, uuid)
  @newtype case class CartItemId(value: UUID)
  @derive(eqv, show, uuid)
  @newtype case class FavoriteId(value: UUID)
  @derive(eqv, show, uuid)
  @newtype case class DiscountId(value: UUID)
  @derive(eqv, show, uuid)
  @newtype case class DiscountUsageId(value: UUID)
  @derive(eqv, show, uuid)
  @newtype case class RatingId(value: UUID)
}
