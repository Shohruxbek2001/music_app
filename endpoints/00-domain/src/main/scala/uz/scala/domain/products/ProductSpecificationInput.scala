package uz.scala.domain.products

import io.circe.generic.JsonCodec

import uz.scala.domain.SpecificationId
import uz.scala.domain.SpecificationValueId
import uz.scala.syntax.circe._

@JsonCodec
case class ProductSpecificationInput(
    specId: SpecificationId,
    valueId: SpecificationValueId,
  )
