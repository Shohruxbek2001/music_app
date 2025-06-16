package uz.scala.repos.dto

import eu.timepit.refined.types.string.NonEmptyString
import io.scalaland.chimney.dsl.TransformationOps

import uz.scala.domain.SpecificationId
import uz.scala.domain.SpecificationValueId
import uz.scala.domain.specifications

case class SpecificationValue(
    id: SpecificationValueId,
    value: NonEmptyString,
    specificationId: SpecificationId,
  ) {
  def toDomain: specifications.SpecificationValue =
    this
      .into[specifications.SpecificationValue]
      .transform
}
