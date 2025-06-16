package uz.scala.domain.enums

import enumeratum.EnumEntry.UpperSnakecase
import enumeratum._

sealed trait UserStatus extends UpperSnakecase
object UserStatus extends Enum[UserStatus] with CirceEnum[UserStatus] with DoobieEnum[UserStatus] {
  case object Active extends UserStatus
  case object Inactive extends UserStatus
  case object Expired extends UserStatus
  override def values: IndexedSeq[UserStatus] = findValues
}
