package uz.scala.repos.dto

import uz.scala.domain.ProductId
import uz.scala.domain.SpecificationId
import uz.scala.domain.SpecificationValueId

case class ProductSpecification(
    productId: ProductId,
    specificationId: SpecificationId,
    specificationValueId: SpecificationValueId,
  )
