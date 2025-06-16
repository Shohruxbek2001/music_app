package uz.scala.domain.events

import java.time.ZonedDateTime

import enumeratum.EnumEntry.UpperSnakecase
import enumeratum._
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Json
import io.circe.generic.JsonCodec
import io.circe.refined._

import uz.scala.domain.MarketId
import uz.scala.domain.OrderId
import uz.scala.domain.events.AppEvent.AppEventKind
import uz.scala.syntax.circe._

@JsonCodec
case class AppEvent(
    kind: AppEventKind,
    data: Json,
    timestamp: ZonedDateTime,
  )

object AppEvent {
  sealed trait AppEventKind extends UpperSnakecase

  object AppEventKind extends Enum[AppEventKind] with CirceEnum[AppEventKind] {
    case object AddOrderItem extends AppEventKind

    override def values: IndexedSeq[AppEventKind] = findValues
  }

  @JsonCodec
  case class AddOrderItemData(
      marketId: MarketId,
      orderId: OrderId,
      barcode: NonEmptyString,
    )
}
