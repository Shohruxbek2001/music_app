package uz.scala.repos.dto

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.ConfiguredJsonCodec
import io.circe.refined._
import io.scalaland.chimney.dsl.TransformationOps

import uz.scala.domain.SpecificationId
import uz.scala.domain.SpecificationValueId
import uz.scala.syntax.circe._

@ConfiguredJsonCodec
case class SpecificationDetails(
    id: SpecificationId,
    name: NonEmptyString,
    valueId: SpecificationValueId,
    value: NonEmptyString,
  ) {
  def toDomain: uz.scala.domain.specifications.SpecificationDetails =
    this
      .into[uz.scala.domain.specifications.SpecificationDetails]
      .transform
}

object SpecificationDetails {
  implicit val config: Configuration =
    Configuration.default.withSnakeCaseMemberNames
}
