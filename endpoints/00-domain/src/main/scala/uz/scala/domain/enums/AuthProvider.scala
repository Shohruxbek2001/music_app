package uz.scala.domain.enums

import enumeratum.EnumEntry.UpperSnakecase
import enumeratum._

sealed trait AuthProvider extends UpperSnakecase
object AuthProvider
    extends Enum[AuthProvider]
       with CirceEnum[AuthProvider]
       with DoobieEnum[AuthProvider] {
  case object Google extends AuthProvider
  case object Local extends AuthProvider
  case object Apple extends AuthProvider
  case object Facebook extends AuthProvider
  override def values: IndexedSeq[AuthProvider] = findValues
}
