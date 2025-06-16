package uz.scala.algebras

import cats.effect.MonadCancelThrow
import cats.effect.std.Random
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps
import cats.~>
import doobie.ConnectionIO
import doobie.syntax.connectionio._
import eu.timepit.refined.types.numeric.NonNegInt
import org.typelevel.log4cats.Logger

import uz.scala.Language
import uz.scala.domain.ProductId
import uz.scala.domain.products.ProductInput
import uz.scala.domain.products.ProductUpdateInput
import uz.scala.effects.Calendar
import uz.scala.effects.GenBarcode
import uz.scala.effects.GenUUID
import uz.scala.repos.ProductsRepository
import uz.scala.repos.SpecificationsRepository
import uz.scala.repos.dto
import uz.scala.syntax.refined._
import uz.scala.utils.ID

trait ProductsAlgebra[F[_]] {
  def create(input: ProductInput)(implicit language: Language): F[ProductId]
  def update(
      id: ProductId,
      input: ProductUpdateInput,
    )(implicit
      language: Language
    ): F[Unit]

  def delete(id: ProductId): F[Unit]
}

object ProductsAlgebra {
  def make[F[_]: MonadCancelThrow: GenUUID: Calendar: Random: GenBarcode](
      productsRepository: ProductsRepository[doobie.ConnectionIO],
      specificationsRepository: SpecificationsRepository[doobie.ConnectionIO],
    )(implicit
      logger: Logger[F],
      xa: doobie.Transactor[F],
      lifter: F ~> ConnectionIO,
    ): ProductsAlgebra[F] =
    new ProductsAlgebra[F] {
      override def create(input: ProductInput)(implicit language: Language): F[ProductId] =
        for {
          id <- ID.make[F, ProductId]
          now <- Calendar[F].currentZonedDateTime
          product = dto.Product(
            id = id,
            slug = slugify(uzTranslit(input.name)),
            name = input.name,
            description = input.description,
            price = input.price,
            discountPrice = input.discountPrice,
            stockQuantity = input.quantity.getOrElse[NonNegInt](0),
            categoryId = input.categoryId,
            marketId = input.marketId,
            createdAt = now,
          )
          productId <- productsRepository.create(product).transact(xa)
        } yield productId

      override def update(
          id: ProductId,
          input: ProductUpdateInput,
        )(implicit
          language: Language
        ): F[Unit] =
        productsRepository
          .update(id) { product =>
            product.copy(
              name = input.name.getOrElse(product.name),
              categoryId = input.categoryId.getOrElse(product.categoryId),
            )
          }
          .transact(xa)

      override def delete(id: ProductId): F[Unit] =
        productsRepository.delete(id).transact(xa)

      private def slugify(name: String): String =
        name
          .toLowerCase()
          .replaceAll("[^a-z0-9\\s-]", "")
          .replaceAll("\\s+", "-")
          .replaceAll("-+", "-")
          .stripPrefix("-")
          .stripSuffix("-")

      private def uzTranslit(name: String): String =
        name
          .replace("o‘", "o")
          .replace("g‘", "g")
          .replace("ʼ", "")
          .replace("’", "")
          .replace("‘", "")
    }
}
