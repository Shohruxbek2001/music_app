package uz.scala.domain.specifications

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._

import uz.scala.domain.SpecificationValueId
import uz.scala.syntax.circe._

@JsonCodec
case class SpecificationValue(
    id: SpecificationValueId,
    value: NonEmptyString,
  )
