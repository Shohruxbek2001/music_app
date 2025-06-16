package uz.scala.repos.dto

import eu.timepit.refined.predicates.all.Url

import uz.scala.domain.ProductId
import uz.scala.domain.ProductImageId

case class ProductImages(
    id: ProductImageId,
    productId: ProductId,
    imageUrl: Url,
    position: Int,
    is_main: Boolean,
  )
