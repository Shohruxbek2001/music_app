package uz.scala.domain.categories

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._

import uz.scala.domain.CategoryId
import uz.scala.syntax.circe._

@JsonCodec
case class CategoryInput(
    name: NonEmptyString,
    parentId: Option[CategoryId],
  )
