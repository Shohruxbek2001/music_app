package uz.scala.auth.utils

import scala.concurrent.duration.FiniteDuration

import cats.effect.Sync
import cats.implicits.toFunctorOps
import pdi.jwt.JwtClaim

trait JwtExpire[F[_]] {
  def expiresIn(claim: JwtClaim, exp: FiniteDuration): F[JwtClaim]
  def isExpired(claim: JwtClaim): F[Boolean]
}

object JwtExpire {
  def apply[F[_]: Sync]: JwtExpire[F] =
    new JwtExpire[F] {
      override def expiresIn(claim: JwtClaim, exp: FiniteDuration): F[JwtClaim] =
        JwtClock[F].utc.map { implicit jClock =>
          claim.issuedNow.expiresIn(exp.toSeconds)
        }

      override def isExpired(claim: JwtClaim): F[Boolean] =
        JwtClock[F].utc.map { implicit jClock =>
          !claim.isValid
        }
    }
}
