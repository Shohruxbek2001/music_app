package uz.scala.repos

import cats.data.NonEmptyList
import cats.data.OptionT
import cats.implicits.catsSyntaxApplicativeErrorId
import cats.implicits.catsSyntaxApplicativeId
import cats.implicits.toFunctorOps
import doobie.ConnectionIO

import uz.scala.Language
import uz.scala.domain.ProductId
import uz.scala.domain.ResponseData
import uz.scala.domain.SpecificationId
import uz.scala.domain.SpecificationValueId
import uz.scala.domain.specifications.SpecificationFilters
import uz.scala.domain.specifications.SpecificationValue
import uz.scala.exception.AError
import uz.scala.repos.sql.ProductSpecificationsSql
import uz.scala.repos.sql.SpecificationValuesSql
import uz.scala.repos.sql.SpecificationsSql
import uz.scala.shared.ResponseMessages.SPECIFICATION_NOT_FOUND
import uz.scala.shared.ResponseMessages.SPECIFICATION_VALUE_NOT_FOUND

trait SpecificationsRepository[F[_]] {
  def create(data: dto.Specification)(implicit language: Language): ConnectionIO[Unit]
  def get(filters: SpecificationFilters): ConnectionIO[ResponseData[dto.Specification]]
  def update(
      id: SpecificationId
    )(
      update: dto.Specification => dto.Specification
    )(implicit
      language: Language
    ): F[Unit]
  def delete(id: SpecificationId): F[Unit]
  def createValue(data: dto.SpecificationValue)(implicit language: Language): ConnectionIO[Unit]
  def findValuesBySpecIds(
      ids: List[SpecificationId]
    ): F[Map[SpecificationId, List[SpecificationValue]]]
  def updateValue(
      id: SpecificationValueId
    )(
      update: dto.SpecificationValue => dto.SpecificationValue
    )(implicit
      language: Language
    ): F[Unit]
  def deleteValue(specId: SpecificationId, valueId: SpecificationValueId): F[Unit]
  def addToProduct(
      data: NonEmptyList[dto.ProductSpecification]
    )(implicit
      language: Language
    ): ConnectionIO[Unit]
  def deleteFromProduct(productId: ProductId, specificationId: SpecificationId): F[Unit]
}

object SpecificationsRepository {
  def make: SpecificationsRepository[ConnectionIO] = new SpecificationsRepository[ConnectionIO] {
    override def create(data: dto.Specification)(implicit language: Language): ConnectionIO[Unit] =
      SpecificationsSql.insert.run(data).void

    override def get(filters: SpecificationFilters): ConnectionIO[ResponseData[dto.Specification]] =
      SpecificationsSql.get(filters).to[List].map { users =>
        ResponseData(users.map(_._1), users.headOption.fold(0L)(_._2))
      }

    override def update(
        id: SpecificationId
      )(
        update: dto.Specification => dto.Specification
      )(implicit
        language: Language
      ): ConnectionIO[Unit] =
      OptionT(SpecificationsSql.findById(id).option).cataF(
        AError.BadRequest(SPECIFICATION_NOT_FOUND(language)).raiseError[ConnectionIO, Unit],
        specification => SpecificationsSql.update(update(specification)).run.void,
      )

    override def delete(id: SpecificationId): ConnectionIO[Unit] =
      SpecificationsSql.delete(id).run.void

    override def createValue(
        data: dto.SpecificationValue
      )(implicit
        language: Language
      ): ConnectionIO[Unit] =
      SpecificationValuesSql.insert.run(data).void

    override def findValuesBySpecIds(
        ids: List[SpecificationId]
      ): ConnectionIO[Map[SpecificationId, List[SpecificationValue]]] =
      NonEmptyList
        .fromList(ids)
        .fold(
          Map.empty[SpecificationId, List[SpecificationValue]].pure[ConnectionIO]
        ) { specificationIds =>
          SpecificationValuesSql
            .findBySpecificationIds(specificationIds)
            .to[List]
            .map(_.groupBy(_.specificationId))
            .map(v => v.map(c => c._1 -> c._2.map(_.toDomain)))
        }

    override def updateValue(
        id: SpecificationValueId
      )(
        update: dto.SpecificationValue => dto.SpecificationValue
      )(implicit
        language: Language
      ): ConnectionIO[Unit] =
      OptionT(SpecificationValuesSql.findById(id).option).cataF(
        AError.BadRequest(SPECIFICATION_VALUE_NOT_FOUND(language)).raiseError[ConnectionIO, Unit],
        data => SpecificationValuesSql.update(update(data)).run.void,
      )

    override def deleteValue(
        specId: SpecificationId,
        valueId: SpecificationValueId,
      ): ConnectionIO[Unit] =
      SpecificationValuesSql.delete(specId, valueId).run.void

    override def addToProduct(
        data: NonEmptyList[dto.ProductSpecification]
      )(implicit
        language: Language
      ): ConnectionIO[Unit] =
      ProductSpecificationsSql.insert.updateMany(data).void

    override def deleteFromProduct(
        productId: ProductId,
        specificationId: SpecificationId,
      ): ConnectionIO[Unit] =
      ProductSpecificationsSql.delete(productId, specificationId).run.void
  }
}
