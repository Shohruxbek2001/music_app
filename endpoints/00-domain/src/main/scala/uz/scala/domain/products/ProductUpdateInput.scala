package uz.scala.domain.products

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._

import uz.scala.domain.CategoryId
import uz.scala.syntax.circe._

@JsonCodec
case class ProductUpdateInput(
    name: Option[NonEmptyString],
    categoryId: Option[CategoryId],
  )
