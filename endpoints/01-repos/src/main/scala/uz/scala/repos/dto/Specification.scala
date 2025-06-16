package uz.scala.repos.dto

import eu.timepit.refined.types.string.NonEmptyString
import io.scalaland.chimney.dsl.TransformationOps

import uz.scala.domain.CategoryId
import uz.scala.domain.SpecificationId
import uz.scala.domain.categories
import uz.scala.domain.specifications

case class Specification(
    id: SpecificationId,
    name: NonEmptyString,
    categoryId: Option[CategoryId],
  ) {
  def toDomain(
      category: Option[categories.Category] = None,
      specificationValues: List[specifications.SpecificationValue] = Nil,
    ): specifications.Specification =
    this
      .into[specifications.Specification]
      .withFieldConst(_.category, category)
      .withFieldConst(_.values, specificationValues)
      .transform
}
