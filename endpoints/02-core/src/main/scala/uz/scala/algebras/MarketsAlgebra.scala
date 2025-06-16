package uz.scala.algebras

import cats.effect.MonadCancelThrow
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps
import doobie.ConnectionIO
import doobie.Transactor
import doobie.syntax.connectionio.toConnectionIOOps

import uz.scala.Language
import uz.scala.domain.MarketId
import uz.scala.domain.markets.Market
import uz.scala.domain.markets.MarketInput
import uz.scala.effects.Calendar
import uz.scala.effects.GenUUID
import uz.scala.repos.MarketsRepository
import uz.scala.repos.dto
import uz.scala.utils.ID

trait MarketsAlgebra[F[_]] {
  def create(input: MarketInput): F[MarketId]
  def update(id: MarketId, input: MarketInput)(implicit language: Language): F[Unit]
  def get: F[List[Market]]
  def findById(id: MarketId): F[Option[Market]]
  def delete(id: MarketId): F[Unit]
}

object MarketsAlgebra {
  def make[F[_]: MonadCancelThrow: GenUUID: Calendar](
      marketRepo: MarketsRepository[ConnectionIO]
    )(implicit
      xa: Transactor[F]
    ): MarketsAlgebra[F] =
    new MarketsAlgebra[F] {
      override def create(input: MarketInput): F[MarketId] =
        for {
          id <- ID.make[F, MarketId]
          now <- Calendar[F].currentZonedDateTime
          market = dto.Market(
            id = id,
            name = input.name,
            description = input.description,
            createdAt = now,
          )
          _ <- marketRepo.create(market).transact(xa)
        } yield id

      override def update(id: MarketId, input: MarketInput)(implicit language: Language): F[Unit] =
        marketRepo
          .update(id) { market =>
            market.copy(
              name = input.name,
              description = input.description,
            )
          }
          .transact(xa)
      override def get: F[List[Market]] =
        marketRepo.get.map(_.map(_.intoDomain)).transact(xa)

      override def findById(id: MarketId): F[Option[Market]] =
        marketRepo.findById(id).map(_.map(_.intoDomain)).transact(xa)

      override def delete(id: MarketId): F[Unit] =
        marketRepo.delete(id: MarketId).transact(xa)
    }
}
