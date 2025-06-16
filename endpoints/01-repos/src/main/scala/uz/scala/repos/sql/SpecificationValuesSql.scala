package uz.scala.repos.sql

import cats.data.NonEmptyList
import doobie._
import doobie.implicits._
import doobie.refined.implicits._
import doobie.util.fragments.in

import uz.scala.domain.SpecificationId
import uz.scala.domain.SpecificationValueId
import uz.scala.doobie.Sql
import uz.scala.doobie.syntax.all._
import uz.scala.repos.dto
import uz.scala.repos.dto.SpecificationValue

object SpecificationValuesSql extends Sql[dto.SpecificationValue] {
  val insert: Update[SpecificationValue] = Update[dto.SpecificationValue](
    sql"""INSERT INTO $table ($columns) VALUES ($values)""".internals.sql
  )

  def findBySpecificationIds(
      ids: NonEmptyList[SpecificationId]
    ): Query0[dto.SpecificationValue] =
    sql"""SELECT $columns FROM $table WHERE ${in(fr"specification_id", ids)}"""
      .query[dto.SpecificationValue]

  def findById(id: SpecificationValueId): Query0[dto.SpecificationValue] =
    sql"""SELECT $columns FROM $table WHERE id = $id""".query[dto.SpecificationValue]

  def update(data: dto.SpecificationValue): Update0 =
    sql"""UPDATE $table SET value = ${data.value} WHERE id = ${data.id}""".update

  def delete(specId: SpecificationId, valueId: SpecificationValueId): Update0 =
    sql"""DELETE FROM $table WHERE specification_id = $specId AND id = $valueId""".update
}
