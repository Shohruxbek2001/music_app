package uz.scala.domain.specifications

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._

import uz.scala.domain.SpecificationId
import uz.scala.domain.categories.Category
import uz.scala.syntax.circe._

@JsonCodec
case class Specification(
    id: SpecificationId,
    name: NonEmptyString,
    category: Option[Category],
    values: List[SpecificationValue],
  )
