package uz.scala.repos.dto

import eu.timepit.refined.types.string.NonEmptyString
import io.scalaland.chimney.dsl.TransformerOps

import uz.scala.domain.MarketId

case class MarketInfo(
    id: MarketId,
    name: NonEmptyString,
  ) {
  def toDomain: uz.scala.domain.markets.MarketInfo =
    this.transformInto[uz.scala.domain.markets.MarketInfo]
}
