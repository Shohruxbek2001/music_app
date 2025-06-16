package uz.scala.repos.sql

import cats.data.NonEmptyList
import cats.implicits.catsSyntaxOptionId
import doobie.Query0
import doobie.Update
import doobie.Update0
import doobie.implicits.toSqlInterpolator
import doobie.refined.implicits._
import doobie.util.fragments.in

import uz.scala.domain.CategoryId
import uz.scala.doobie.Sql
import uz.scala.doobie.syntax.all._
import uz.scala.repos.dto

private[repos] object CategoriesSql extends Sql[dto.Category]("categories".some) {
  val insert: Update[dto.Category] = Update[dto.Category](
    sql"""INSERT INTO $table ($columns) VALUES (?,?,?)""".internals.sql
  )

  val selectAll: Query0[dto.Category] =
    sql"""SELECT $columns FROM $table""".query[dto.Category]

  def findByIds(
      ids: NonEmptyList[CategoryId]
    ): Query0[dto.Category] =
    sql"""SELECT $columns FROM $table WHERE ${in(fr"id", ids)}"""
      .query[dto.Category]

  def findById(id: CategoryId): Query0[dto.Category] =
    sql"""SELECT $columns FROM $table WHERE id = $id LIMIT 1"""
      .query[dto.Category]

  def update(category: dto.Category): Update0 =
    sql"""UPDATE $table SET name = ${category.name}, parent_id = ${category.parentId} WHERE id = ${category.id}""".update

  def delete(id: CategoryId): Update0 =
    sql"""DELETE FROM $table WHERE id = $id""".update
}
