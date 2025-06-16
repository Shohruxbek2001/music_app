package uz.scala.domain.markets

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._

@JsonCodec
case class MarketInput(
    name: NonEmptyString,
    description: Option[NonEmptyString],
  )
