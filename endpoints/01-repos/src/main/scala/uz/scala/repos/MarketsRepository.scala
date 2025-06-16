package uz.scala.repos

import cats.implicits.toFunctorOps
import doobie.ConnectionIO
import uz.scala.Language
import uz.scala.domain.MarketId
import uz.scala.exception.AError
import uz.scala.repos.dto.Market
import uz.scala.repos.sql.MarketsSql
import uz.scala.shared.ResponseMessages.MARKET_NOT_FOUND
import uz.scala.syntax.all.optionSyntaxFunctorOptionOps

trait MarketsRepository[F[_]] {
  def create(market: dto.Market): F[Unit]
  def update(
      id: MarketId
    )(
      update: dto.Market => dto.Market
    )(implicit
      language: Language
    ): F[Unit]
  def get: F[List[dto.Market]]
  def findById(id: MarketId): F[Option[dto.Market]]
  def delete(id: MarketId): F[Unit]
}

object MarketsRepository {
  def make: MarketsRepository[ConnectionIO] =
    new MarketsRepository[ConnectionIO] {
      override def create(market: dto.Market): ConnectionIO[Unit] =
        MarketsSql.insert.run(market).void

      override def update(
          id: MarketId
        )(
          update: dto.Market => dto.Market
        )(implicit
          language: Language
        ): ConnectionIO[Unit] =
        findById(id)
          .getOrRaise(AError.BadRequest(MARKET_NOT_FOUND(language)))
          .flatMap { market =>
            MarketsSql.update(update(market)).run.void
          }

      override def get: ConnectionIO[List[dto.Market]] =
        MarketsSql.get.to[List]

      override def findById(id: MarketId): ConnectionIO[Option[Market]] =
        MarketsSql.findById(id).option

      override def delete(id: MarketId): ConnectionIO[Unit] =
        MarketsSql.delete(id).run.void
    }
}
