package uz.scala.repos.dto

import java.time.ZonedDateTime

import eu.timepit.refined.types.string.NonEmptyString
import io.scalaland.chimney.dsl.TransformerOps

import uz.scala.domain.MarketId

case class Market(
    id: MarketId,
    name: NonEmptyString,
    description: Option[NonEmptyString],
    createdAt: ZonedDateTime,
    updatedAt: Option[ZonedDateTime] = None,
    deletedAt: Option[ZonedDateTime] = None,
  ) {
  def intoDomain: uz.scala.domain.markets.Market =
    this.transformInto[uz.scala.domain.markets.Market]

  def info: MarketInfo = MarketInfo(
    id = id,
    name = name,
  )
}
