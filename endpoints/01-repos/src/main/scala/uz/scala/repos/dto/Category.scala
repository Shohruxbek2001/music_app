package uz.scala.repos.dto

import eu.timepit.refined.types.string.NonEmptyString
import io.scalaland.chimney.dsl.TransformationOps

import uz.scala.domain.CategoryId
import uz.scala.domain.categories

case class Category(
    id: CategoryId,
    name: NonEmptyString,
    parentId: Option[CategoryId],
  ) {
  def toDomain(
      subCategories: Option[List[categories.Category]] = None
    ): categories.Category =
    this
      .into[categories.Category]
      .withFieldConst(_.subcategories, subCategories)
      .transform
}
