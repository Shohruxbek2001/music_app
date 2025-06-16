package uz.scala.domain.specifications

import eu.timepit.refined.types.numeric.NonNegInt
import io.circe.generic.JsonCodec
import io.circe.refined._

import uz.scala.domain.CategoryId
import uz.scala.syntax.circe._

@JsonCodec
case class SpecificationFilters(
    categoryId: Option[CategoryId] = None,
    uncategorized: Option[Boolean] = None,
    limit: Option[NonNegInt] = None,
    page: Option[NonNegInt] = None,
  )
