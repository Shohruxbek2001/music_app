package uz.scala.repos.sql

import doobie.Query0
import doobie.Update
import doobie.Update0
import doobie.implicits.toSqlInterpolator
import doobie.refined.implicits._

import uz.scala.domain.OrderItemId
import uz.scala.doobie.Sql
import uz.scala.doobie.syntax.all._
import uz.scala.repos.dto

object OrdersItemsSql extends Sql[dto.OrderItem] {
  val insert: Update[dto.OrderItem] =
    Update[dto.OrderItem](
      sql"""INSERT INTO $table ($columns) VALUES ($values) ON CONFLICT (order_id, product_variant_id) DO UPDATE
            SET quantity = $table.quantity + EXCLUDED.quantity,
                unit_price = EXCLUDED.unit_price,
                unit_price_after_discount = EXCLUDED.unit_price_after_discount,
                total_price = $table.total_price + EXCLUDED.total_price
        """
        .internals
        .sql
    )

  def findById(id: OrderItemId): Query0[dto.OrderItem] =
    sql"""SELECT $columns FROM $table WHERE id = $id LIMIT 1"""
      .query[dto.OrderItem]


  def update(item: dto.OrderItem): Update0 =
    sql"""UPDATE $table 
           SET quantity = ${item.quantity}, 
               unit_price = ${item.unitPrice},
               unit_price_after_discount = ${item.unitPrice},
               total_price = ${item.totalPrice}
           WHERE id = ${item.id}
        """.update

  def delete(itemId: OrderItemId): Update0 =
    sql"""DELETE FROM $table WHERE id = $itemId""".update
}
