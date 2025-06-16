package uz.scala.auth.impl

import cats.data.OptionT
import cats.effect.Sync
import cats.implicits._
import dev.profunktor.auth.jwt.JwtAuth
import dev.profunktor.auth.jwt.JwtToken
import org.http4s.server
import pdi.jwt.JwtAlgorithm

import uz.scala.auth.AuthConfig
import uz.scala.auth.utils.AuthMiddleware
import uz.scala.auth.utils.JwtAuthMiddleware
import uz.scala.domain.AuthedUser
import uz.scala.redis.RedisClient
import uz.scala.syntax.all.circeSyntaxDecoderOps
import uz.scala.syntax.refined.commonSyntaxAutoUnwrapV

object LiveMiddleware {
  def make[F[_]: Sync](
      jwtConfig: AuthConfig,
      redis: RedisClient[F],
    ): server.AuthMiddleware[F, AuthedUser] = {
    val userJwtAuth = JwtAuth.hmac(jwtConfig.tokenKey.toCharArray, JwtAlgorithm.HS256)
    def findUser(token: String): F[Option[AuthedUser]] =
      OptionT(redis.get(token))
        .semiflatMap(_.decodeAsF[F, AuthedUser])
        .value

    def destroySession(token: JwtToken): F[Unit] =
      OptionT(findUser(AuthMiddleware.ACCESS_TOKEN_PREFIX + token.value))
        .semiflatMap(user =>
          redis.del(AuthMiddleware.ACCESS_TOKEN_PREFIX + token.value, user.email)
        )
        .value
        .void

    AuthMiddleware[F, AuthedUser](
      userJwtAuth,
      findUser,
      destroySession,
    )
  }

  def makeForApp[F[_]: Sync](
      jwtConfig: AuthConfig
    ): server.AuthMiddleware[F, Unit] =
    JwtAuthMiddleware[F, Unit](
      JwtAuth.hmac(jwtConfig.appTokenKey.toCharArray, JwtAlgorithm.HS256),
      token => (token.value == jwtConfig.appToken.value).guard[Option].pure[F],
    )
}
