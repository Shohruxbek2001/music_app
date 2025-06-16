package uz.scala.domain.markets

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._

import uz.scala.domain.MarketId
import uz.scala.syntax.circe._

@JsonCodec
case class MarketInfo(
    id: MarketId,
    name: NonEmptyString,
  )
