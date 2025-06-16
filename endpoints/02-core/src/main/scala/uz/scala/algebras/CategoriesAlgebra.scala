package uz.scala.algebras

import cats.effect.MonadCancelThrow
import cats.effect.std.Random
import cats.implicits.catsSyntaxOptionId
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps
import doobie.syntax.connectionio._
import org.typelevel.log4cats.Logger

import uz.scala.Language
import uz.scala.domain.CategoryId
import uz.scala.domain.categories.Category
import uz.scala.domain.categories.CategoryInput
import uz.scala.effects.Calendar
import uz.scala.effects.GenUUID
import uz.scala.repos.CategoriesRepository
import uz.scala.repos.dto
import uz.scala.utils.ID

trait CategoriesAlgebra[F[_]] {
  def create(input: CategoryInput): F[CategoryId]
  def getAll: F[List[Category]]
  def update(
      id: CategoryId,
      input: CategoryInput,
    )(implicit
      language: Language
    ): F[Unit]
  def delete(id: CategoryId): F[Unit]
}

object CategoriesAlgebra {
  def make[F[_]: MonadCancelThrow: GenUUID: Calendar: Random](
      categoriesRepository: CategoriesRepository[doobie.ConnectionIO]
    )(implicit
      logger: Logger[F],
      xa: doobie.Transactor[F],
    ): CategoriesAlgebra[F] =
    new CategoriesAlgebra[F] {
      override def create(input: CategoryInput): F[CategoryId] =
        for {
          id <- ID.make[F, CategoryId]
          category = dto.Category(
            id = id,
            name = input.name,
            parentId = input.parentId,
          )
          _ <- categoriesRepository.create(category).transact(xa)
        } yield id

      override def getAll: F[List[Category]] =
        categoriesRepository.getAll.map(buildCategoryTree).transact(xa)

      override def update(
          id: CategoryId,
          input: CategoryInput,
        )(implicit
          language: Language
        ): F[Unit] =
        for {
          _ <- logger.info(s"Updating category.. $input")
          _ <- categoriesRepository
            .update(id)(c =>
              c.copy(name = input.name, parentId = input.parentId.orElse(c.parentId))
            )
            .transact(xa)
        } yield ()

      override def delete(id: CategoryId): F[Unit] =
        categoriesRepository.delete(id).transact(xa)

      private def buildCategoryTree(
          categories: List[dto.Category]
        ): List[Category] = {

        val groupedByParent = categories.groupBy(_.parentId)

        def buildTree(parentId: Option[CategoryId]): List[Category] =
          groupedByParent.getOrElse(parentId, Nil).map { category =>
            val subcategories = buildTree(category.id.some)
            Category(
              id = category.id,
              name = category.name,
              parentId = parentId,
              subcategories = Option.when(subcategories.nonEmpty)(subcategories),
            )
          }

        buildTree(None)
      }
    }
}
