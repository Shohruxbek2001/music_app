package uz.scala.repos

import cats.data.NonEmptyList
import cats.implicits.catsSyntaxApplicativeId
import cats.implicits.toFunctorOps
import doobie.ConnectionIO

import uz.scala.Language
import uz.scala.domain.CategoryId
import uz.scala.domain.categories.Category
import uz.scala.exception.AError
import uz.scala.repos.sql.CategoriesSql
import uz.scala.shared.ResponseMessages.CATEGORY_NOT_FOUND
import uz.scala.syntax.all.optionSyntaxFunctorOptionOps

trait CategoriesRepository[F[_]] {
  def create(category: dto.Category): F[Unit]
  def getAll: F[List[dto.Category]]
  def findByIds(
      ids: List[CategoryId]
    ): F[Map[CategoryId, Category]]
  def update(
      id: CategoryId
    )(
      update: dto.Category => dto.Category
    )(implicit
      language: Language
    ): F[Unit]
  def delete(id: CategoryId): F[Unit]
}

object CategoriesRepository {
  def make: CategoriesRepository[ConnectionIO] =
    new CategoriesRepository[ConnectionIO] {
      override def create(category: dto.Category): ConnectionIO[Unit] =
        CategoriesSql.insert.run(category).void

      override def findByIds(
          ids: List[CategoryId]
        ): ConnectionIO[Map[CategoryId, Category]] =
        NonEmptyList
          .fromList(ids)
          .fold(
            Map.empty[CategoryId, Category].pure[ConnectionIO]
          ) { categoryIds =>
            CategoriesSql
              .findByIds(categoryIds)
              .to[List]
              .map(_.map(c => c.id -> c.toDomain()).toMap)
          }

      override def getAll: ConnectionIO[List[dto.Category]] =
        CategoriesSql.selectAll.to[List]

      override def update(
          id: CategoryId
        )(
          update: dto.Category => dto.Category
        )(implicit
          language: Language
        ): ConnectionIO[Unit] =
        CategoriesSql
          .findById(id)
          .option
          .getOrRaise(AError.BadRequest(CATEGORY_NOT_FOUND(language)))
          .flatMap { category =>
            CategoriesSql.update(update(category)).run.void
          }

      override def delete(id: CategoryId): ConnectionIO[Unit] =
        CategoriesSql.delete(id).run.void
    }
}
