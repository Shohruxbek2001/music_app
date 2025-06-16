package uz.scala.repos.dto

import java.time.ZonedDateTime

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Json
import squants.Money

import uz.scala.domain.AddressId
import uz.scala.domain.CustomerId
import uz.scala.domain.DiscountId
import uz.scala.domain.OrderId
import uz.scala.domain.enums.OrderStatus
import uz.scala.domain.enums.PaymentMethod

case class Order(
    id: OrderId,
    customerId: CustomerId,
    addressId: Option[AddressId],
    totalPrice: Money,
    discountId: Option[DiscountId],
    status: OrderStatus = OrderStatus.New,
    orderNumber: Option[NonEmptyString],
    addressSnapshot: Json,
    paymentMethod: PaymentMethod,
    createdAt: ZonedDateTime,
    updatedAt: Option[ZonedDateTime] = None,
    deletedAt: Option[ZonedDateTime] = None,
  )
