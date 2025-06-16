package uz.scala.repos.sql

import doobie.Query0
import doobie.Update
import doobie.Update0
import doobie.implicits.toSqlInterpolator
import doobie.refined.implicits._

import uz.scala.domain.MarketId
import uz.scala.doobie.Sql
import uz.scala.doobie.syntax.all._
import uz.scala.repos.dto

object MarketsSql extends Sql[dto.Market] {
  val insert: Update[dto.Market] =
    Update[dto.Market](
      sql"""INSERT INTO $table ($columns) VALUES ($values)"""
        .internals
        .sql
    )

  def update(market: dto.Market): Update0 =
    sql"""UPDATE $table
        SET name = ${market.name},
            description = ${market.description},
            updated_at = now()
        WHERE id = ${market.id}""".update

  val get: Query0[dto.Market] =
    sql"""SELECT $columns FROM $table WHERE deleted_at IS NULL""".query[dto.Market]

  def findById(id: MarketId): Query0[dto.Market] =
    sql"""SELECT $columns FROM $table WHERE id = $id AND deleted_at IS NULL""".query[dto.Market]

  def delete(id: MarketId): Update0 =
    sql"""UPDATE $table SET deleted_at = now() WHERE id = $id""".update
}
