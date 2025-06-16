package uz.scala.domain.enums

import enumeratum.EnumEntry.UpperSnakecase
import enumeratum._

sealed trait OrderStatus extends UpperSnakecase
object OrderStatus
    extends Enum[OrderStatus]
       with CirceEnum[OrderStatus]
       with DoobieEnum[OrderStatus] {
  case object New extends OrderStatus
  case object Pending extends OrderStatus
  case object PendingPayment extends OrderStatus
  case object Paid extends OrderStatus
  case object Cancelled extends OrderStatus
  override def values: IndexedSeq[OrderStatus] = findValues
}
