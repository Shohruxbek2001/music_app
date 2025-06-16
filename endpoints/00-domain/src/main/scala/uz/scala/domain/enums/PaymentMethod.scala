package uz.scala.domain.enums

import enumeratum.EnumEntry.UpperSnakecase
import enumeratum._

sealed trait PaymentMethod extends UpperSnakecase
object PaymentMethod
    extends Enum[PaymentMethod]
       with CirceEnum[PaymentMethod]
       with DoobieEnum[PaymentMethod] {
  case object Card extends PaymentMethod
  case object Cash extends PaymentMethod
  case object P2P extends PaymentMethod {
    override def entryName: String = "P2P"
  }
  override def values: IndexedSeq[PaymentMethod] = findValues
}
