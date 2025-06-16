package uz.scala.repos.sql

import doobie.Query0
import doobie.Update
import doobie.Update0
import doobie.implicits.toSqlInterpolator
import doobie.postgres.circe.json.implicits._
import doobie.refined.implicits._

import uz.scala.domain.MarketId
import uz.scala.domain.OrderId
import uz.scala.domain.enums.OrderStatus
import uz.scala.doobie.Sql
import uz.scala.doobie.syntax.all._
import uz.scala.repos.dto

object OrdersSql extends Sql[dto.Order] {
  val insert: Update[dto.Order] =
    Update[dto.Order](
      sql"""INSERT INTO $table ($columns) VALUES ($values)"""
        .internals
        .sql
    )

  val nextOrderNumber: Query0[Int] =
    sql"SELECT nextval('orders_order_number_seq')".query[Int]

  def delete(marketId: MarketId, orderId: OrderId): Update0 =
    sql"""UPDATE $table SET deleted_at = NOW() WHERE market_id = $marketId AND id = $orderId""".update

  def updateStatus(orderId: OrderId, status: OrderStatus): Update0 =
    sql"""UPDATE $table SET status = $status WHERE id = $orderId""".update
}
