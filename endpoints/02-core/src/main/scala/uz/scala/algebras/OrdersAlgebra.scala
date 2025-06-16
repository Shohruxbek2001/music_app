package uz.scala.algebras

import java.time.format.DateTimeFormatter

import cats.effect.MonadCancelThrow
import cats.effect.std.Random
import doobie.ConnectionIO
import doobie.Transactor
import eu.timepit.refined.types.string.NonEmptyString

import uz.scala.effects.Calendar
import uz.scala.effects.GenUUID
import uz.scala.repos.OrdersRepository
import uz.scala.syntax.refined.commonSyntaxAutoRefineV

trait OrdersAlgebra[F[_]] {}

object OrdersAlgebra {
  def make[F[_]: MonadCancelThrow: GenUUID: Calendar: Random](
      ordersRepo: OrdersRepository[ConnectionIO],
    )(implicit
      xa: Transactor[F]
    ): OrdersAlgebra[F] =
    new OrdersAlgebra[F] {
      private def makeOrderNumber: ConnectionIO[NonEmptyString] =
        for {
          now <- Calendar[ConnectionIO].currentDate
          seq <- ordersRepo.nextOrderNumber
          datePart = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
          paddedSeq = f"$seq%05d"
          orderNumber = s"$datePart$paddedSeq"
        } yield orderNumber
    }
}
