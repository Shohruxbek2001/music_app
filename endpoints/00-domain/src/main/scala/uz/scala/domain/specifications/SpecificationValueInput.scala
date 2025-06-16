package uz.scala.domain.specifications

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._

@JsonCodec
case class SpecificationValueInput(
    value: NonEmptyString
  )
