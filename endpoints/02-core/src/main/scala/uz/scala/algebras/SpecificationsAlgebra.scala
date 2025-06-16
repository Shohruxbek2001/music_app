package uz.scala.algebras

import cats.data.NonEmptyList
import cats.effect.MonadCancelThrow
import cats.implicits._
import doobie.syntax.connectionio._
import eu.timepit.refined.types.string.NonEmptyString
import org.typelevel.log4cats.Logger

import uz.scala.Language
import uz.scala.domain.ProductId
import uz.scala.domain.ResponseData
import uz.scala.domain.SpecificationId
import uz.scala.domain.SpecificationValueId
import uz.scala.domain.products.ProductSpecificationInput
import uz.scala.domain.specifications.Specification
import uz.scala.domain.specifications.SpecificationFilters
import uz.scala.domain.specifications.SpecificationInput
import uz.scala.domain.specifications.UpdateSpecificationInput
import uz.scala.effects.Calendar
import uz.scala.effects.GenUUID
import uz.scala.repos.CategoriesRepository
import uz.scala.repos.SpecificationsRepository
import uz.scala.repos.dto
import uz.scala.utils.ID

trait SpecificationsAlgebra[F[_]] {
  def create(input: SpecificationInput)(implicit language: Language): F[SpecificationId]
  def get(filters: SpecificationFilters): F[ResponseData[Specification]]
  def update(
      id: SpecificationId,
      input: UpdateSpecificationInput,
    )(implicit
      language: Language
    ): F[Unit]
  def delete(id: SpecificationId): F[Unit]
  def createValue(
      specificationId: SpecificationId,
      value: NonEmptyString,
    )(implicit
      language: Language
    ): F[SpecificationValueId]
  def updateValue(
      specId: SpecificationId,
      valueId: SpecificationValueId,
      value: NonEmptyString,
    )(implicit
      language: Language
    ): F[Unit]
  def deleteValue(specId: SpecificationId, valueId: SpecificationValueId): F[Unit]
  def addToProduct(
      productId: ProductId,
      specification: ProductSpecificationInput,
    )(implicit
      language: Language
    ): F[Unit]
  def deleteFromProduct(productId: ProductId, specificationId: SpecificationId): F[Unit]
}

object SpecificationsAlgebra {
  def make[F[_]: MonadCancelThrow: Calendar: GenUUID](
      specificationsRepository: SpecificationsRepository[doobie.ConnectionIO],
      categoriesRepository: CategoriesRepository[doobie.ConnectionIO],
    )(implicit
      logger: Logger[F],
      xa: doobie.Transactor[F],
    ): SpecificationsAlgebra[F] =
    new SpecificationsAlgebra[F] {
      override def create(
          input: SpecificationInput
        )(implicit
          language: Language
        ): F[SpecificationId] =
        for {
          _ <- logger.info(s"Creating specification.. $input")
          id <- ID.make[F, SpecificationId]
          data = dto.Specification(
            id = id,
            name = input.name,
            categoryId = input.categoryId,
          )
          _ <- specificationsRepository.create(data).transact(xa)
        } yield id

      override def get(filters: SpecificationFilters): F[ResponseData[Specification]] = (for {
        specifications <- specificationsRepository.get(filters)
        categories <- categoriesRepository.findByIds(
          specifications.data.flatMap(_.categoryId)
        )
        specificationValues <- specificationsRepository.findValuesBySpecIds(
          specifications.data.map(_.id)
        )
        result = specifications.copy(data =
          specifications
            .data
            .map(s =>
              s.toDomain(
                category = s.categoryId.flatMap(categories.get),
                specificationValues = specificationValues.getOrElse(s.id, Nil),
              )
            )
        )
      } yield result)
        .transact(xa)

      override def update(
          id: SpecificationId,
          input: UpdateSpecificationInput,
        )(implicit
          language: Language
        ): F[Unit] =
        for {
          _ <- logger.info(s"Updating specification.. $input")
          _ <- specificationsRepository
            .update(id) { sv =>
              sv.copy(
                name = input.name.getOrElse(sv.name),
                categoryId = input.categoryId,
              )
            }
            .transact(xa)
          _ <- logger.info(s"Specification updated with id $id")
        } yield {}

      override def delete(id: SpecificationId): F[Unit] =
        specificationsRepository.delete(id).transact(xa)

      override def createValue(
          specificationId: SpecificationId,
          value: NonEmptyString,
        )(implicit
          language: Language
        ): F[SpecificationValueId] =
        for {
          _ <- logger.info(s"Creating specification value.. $value")
          id <- ID.make[F, SpecificationValueId]
          data = dto.SpecificationValue(
            id = id,
            value = value,
            specificationId = specificationId,
          )
          _ <- specificationsRepository.createValue(data).transact(xa)
        } yield id

      override def updateValue(
          specId: SpecificationId,
          valueId: SpecificationValueId,
          value: NonEmptyString,
        )(implicit
          language: Language
        ): F[Unit] =
        for {
          _ <- logger.info(s"Updating specification value.. $value")
          _ <- specificationsRepository
            .updateValue(valueId) {
              _.copy(value = value)
            }
            .transact(xa)
          _ <- logger.info(s"Specification value updated with id $valueId")
        } yield {}

      override def deleteValue(specId: SpecificationId, valueId: SpecificationValueId): F[Unit] =
        specificationsRepository.deleteValue(specId, valueId).transact(xa)

      override def addToProduct(
          productId: ProductId,
          specification: ProductSpecificationInput,
        )(implicit
          language: Language
        ): F[Unit] =
        for {
          _ <- logger.info(s"Creating product specification.. $specification")
          data = dto.ProductSpecification(
            productId = productId,
            specificationId = specification.specId,
            specificationValueId = specification.valueId,
          )
          _ <- specificationsRepository.addToProduct(NonEmptyList.one(data)).transact(xa)
        } yield ()

      override def deleteFromProduct(
          productId: ProductId,
          specificationId: SpecificationId,
        ): F[Unit] =
        specificationsRepository.deleteFromProduct(productId, specificationId).transact(xa)
    }
}
