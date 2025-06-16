package uz.scala.repos

import cats.data.OptionT
import cats.implicits._
import doobie.ConnectionIO

import uz.scala.Language
import uz.scala.domain.ProductId
import uz.scala.domain.ResponseData
import uz.scala.domain.products.ProductFilters
import uz.scala.effects.Calendar
import uz.scala.exception.AError
import uz.scala.repos.sql.ProductsSql
import uz.scala.shared.ResponseMessages.PRODUCT_NOT_FOUND
import uz.scala.doobie.syntax.all._

trait ProductsRepository[F[_]] {
  def findById(id: ProductId): F[Option[dto.Product]]
  def get(filters: ProductFilters): F[ResponseData[dto.ProductDetails]]
  def create(product: dto.Product): F[ProductId]
  def update(
      id: ProductId
    )(
      update: dto.Product => dto.Product
    )(implicit
      language: Language
    ): F[Unit]
  def delete(id: ProductId): F[Unit]
  def nextSkuNumber: F[Int]
}

object ProductsRepository {
  def make: ProductsRepository[ConnectionIO] = new ProductsRepository[ConnectionIO] {
    override def findById(id: ProductId): ConnectionIO[Option[dto.Product]] =
      ProductsSql.findById(id).option

    override def get(filters: ProductFilters): ConnectionIO[ResponseData[dto.ProductDetails]] =
      ProductsSql.get(filters).to[List].map { products =>
        ResponseData(products.map(_._1), products.headOption.fold(0L)(_._2))
      }

    override def create(
        product: dto.Product
      ): ConnectionIO[ProductId] =
      ProductsSql.insert.withUniqueGeneratedKeys[ProductId]("id")(product)

    override def nextSkuNumber: ConnectionIO[Int] =
      ProductsSql.nextSkuNumber.unique

    override def update(
        id: ProductId
      )(
        update: dto.Product => dto.Product
      )(implicit
        language: Language
      ): ConnectionIO[Unit] =
      OptionT(findById(id)).cataF(
        AError.Internal(PRODUCT_NOT_FOUND(language)).raiseError[ConnectionIO, Unit],
        product =>
          Calendar[ConnectionIO]
            .currentZonedDateTime
            .flatMap(now => ProductsSql.update(update(product.copy(updatedAt = now.some))).run.void),
      )

    override def delete(id: ProductId): ConnectionIO[Unit] =
      ProductsSql.delete(id).run.void
  }
}
