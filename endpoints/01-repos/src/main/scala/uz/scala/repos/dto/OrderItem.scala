package uz.scala.repos.dto

import java.time.ZonedDateTime

import eu.timepit.refined.types.numeric.PosInt
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.ConfiguredJsonCodec
import io.circe.refined._
import squants.Money

import uz.scala.domain.OrderId
import uz.scala.domain.OrderItemId
import uz.scala.domain.ProductVariantId
import uz.scala.syntax.circe._

@ConfiguredJsonCodec
case class OrderItem(
    id: OrderItemId,
    orderId: OrderId,
    createdAt: ZonedDateTime,
    productVariantId: ProductVariantId,
    quantity: PosInt,
    unitPrice: Money,
    unitPriceAfterDiscount: Option[Money],
    totalPrice: Money,
  )
object OrderItem {
  implicit val config: Configuration =
    Configuration.default.withSnakeCaseMemberNames
}
