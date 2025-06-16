package uz.scala.repos.sql

import doobie._
import doobie.implicits._
import doobie.refined.implicits._
import doobie.util.fragments.whereOrOpt

import uz.scala.domain.SpecificationId
import uz.scala.domain.specifications.SpecificationFilters
import uz.scala.doobie.Sql
import uz.scala.doobie.syntax.all._
import uz.scala.repos.dto
import uz.scala.repos.dto.Specification

object SpecificationsSql extends Sql[dto.Specification] {
  val insert: Update[Specification] = Update[dto.Specification](
    sql"""INSERT INTO $table ($columns) VALUES ($values)""".internals.sql
  )

  def get(filters: SpecificationFilters): Query0[(dto.Specification, Long)] = {
    val filtersList = List(
      filters.categoryId.map(categoryId => fr"category_id = $categoryId"),
      filters
        .uncategorized
        .map(uncategorized =>
          if (uncategorized) fr"""category_id IS NULL"""
          else fr"""category_id IS NOT NULL"""
        ),
    ).flatten

    sql"""SELECT $columns, COUNT(*) OVER() AS total FROM $table ${whereOrOpt(filtersList)}"""
      .paginateOpt(filters.limit, filters.page)
      .query[(dto.Specification, Long)]
  }

  def findById(id: SpecificationId): Query0[dto.Specification] =
    sql"""SELECT $columns FROM $table WHERE id = $id""".query[dto.Specification]

  def update(data: dto.Specification): Update0 =
    sql"""UPDATE $table
        SET name = ${data.name}, category_id = ${data.categoryId}
        WHERE id = ${data.id}""".update

  def delete(id: SpecificationId): Update0 =
    sql"""DELETE FROM $table WHERE id = $id""".update
}
