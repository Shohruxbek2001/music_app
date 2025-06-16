package uz.scala.repos.dto

import java.time.ZonedDateTime

import uz.scala.domain.CustomerId
import uz.scala.domain.DiscountId
import uz.scala.domain.DiscountUsageId
import uz.scala.domain.OrderId

case class DiscountUsage(
    id: DiscountUsageId,
    discountId: DiscountId,
    customerId: CustomerId,
    orderId: OrderId,
    usedAt: ZonedDateTime,
  )
