package uz.scala.domain.products

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._

import uz.scala.domain.SpecificationId
import uz.scala.domain.specifications.SpecificationValue
import uz.scala.syntax.circe._

@JsonCodec
case class ProductSpecification(
    id: SpecificationId,
    name: NonEmptyString,
    value: SpecificationValue,
  )
