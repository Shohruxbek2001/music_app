package uz.scala.repos

import cats.implicits.catsSyntaxApplicativeErrorId
import cats.implicits.toFunctorOps
import doobie.ConnectionIO
import doobie.implicits.toDoobieApplicativeErrorOps
import doobie.postgres.sqlstate

import uz.scala.Language
import uz.scala.domain.MarketId
import uz.scala.domain.OrderId
import uz.scala.domain.OrderItemId
import uz.scala.domain.enums.OrderStatus
import uz.scala.exception.AError
import uz.scala.repos.sql.OrdersItemsSql
import uz.scala.repos.sql.OrdersSql
import uz.scala.shared.ResponseMessages.MARKET_NOT_FOUND
import uz.scala.shared.ResponseMessages.ORDER_ITEM_NOT_FOUND
import uz.scala.syntax.all.optionSyntaxFunctorOptionOps

trait OrdersRepository[F[_]] {
  def create(order: dto.Order)(implicit language: Language): F[Unit]
  def addItem(item: dto.OrderItem): F[Unit]
  def updateItem(
      itemId: OrderItemId
    )(
      update: dto.OrderItem => dto.OrderItem
    )(implicit
      lang: Language
    ): F[Unit]
  def deleteItem(itemId: OrderItemId)(implicit lang: Language): F[Unit]
  def nextOrderNumber: F[Int]
  def delete(marketId: MarketId, orderId: OrderId): F[Unit]
  def updateStatus(orderId: OrderId, status: OrderStatus): F[Unit]
}

object OrdersRepository {
  def make: OrdersRepository[ConnectionIO] =
    new OrdersRepository[ConnectionIO] {
      override def create(order: dto.Order)(implicit language: Language): ConnectionIO[Unit] =
        OrdersSql
          .insert
          .run(order)
          .void
          .exceptSomeSqlState {
            case sqlstate.class23.FOREIGN_KEY_VIOLATION =>
              AError.BadRequest(MARKET_NOT_FOUND(language)).raiseError[ConnectionIO, Unit]
          }

      override def addItem(item: dto.OrderItem): ConnectionIO[Unit] =
        OrdersItemsSql.insert.run(item).void

      private def findItem(itemId: OrderItemId): ConnectionIO[Option[dto.OrderItem]] =
        OrdersItemsSql.findById(itemId).option

      override def updateItem(
          itemId: OrderItemId
        )(
          update: dto.OrderItem => dto.OrderItem
        )(implicit
          lang: Language
        ): ConnectionIO[Unit] =
        findItem(itemId)
          .getOrRaise(AError.BadRequest(ORDER_ITEM_NOT_FOUND(lang)))
          .flatMap { item =>
            OrdersItemsSql.update(update(item)).run.void
          }

      override def deleteItem(itemId: OrderItemId)(implicit lang: Language): ConnectionIO[Unit] =
        findItem(itemId)
          .getOrRaise(AError.BadRequest(ORDER_ITEM_NOT_FOUND(lang)))
          .flatMap { _ =>
            OrdersItemsSql.delete(itemId).run.void
          }

      override def nextOrderNumber: ConnectionIO[Int] =
        OrdersSql.nextOrderNumber.unique

      override def delete(marketId: MarketId, orderId: OrderId): ConnectionIO[Unit] =
        OrdersSql.delete(marketId, orderId).run.void

      override def updateStatus(orderId: OrderId, status: OrderStatus): ConnectionIO[Unit] =
        OrdersSql.updateStatus(orderId, status).run.void
    }
}
