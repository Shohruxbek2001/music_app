package uz.scala.routes

import cats.MonadThrow
import cats.implicits.catsSyntaxApplyOps
import cats.implicits.toFlatMapOps
import org.http4s.AuthedRoutes
import org.http4s.HttpRoutes
import org.http4s.circe.JsonDecoder
import org.typelevel.log4cats.Logger

import uz.scala.Language
import uz.scala.auth.impl.Auth
import uz.scala.domain.AuthedUser
import uz.scala.domain.auth._
import uz.scala.http4s.syntax.all.deriveEntityEncoder
import uz.scala.http4s.syntax.all.http4SyntaxReqOps
import uz.scala.http4s.utils.Routes
import uz.scala.syntax.refined._

final case class AuthRoutes[F[_]: Logger: JsonDecoder: MonadThrow](
    auth: Auth[F, AuthedUser]
  ) extends Routes[F, AuthedUser] {
  override val path = "/auth"

  override val public: HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ POST -> Root / "login" =>
        implicit val language: Language = req.lang
        req.decodeR[UserCredentials] { credentials =>
          auth
            .loginByPassword(credentials)
            .flatMap(Ok(_))
        }

      case req @ GET -> Root / "refresh" =>
        implicit val language: Language = req.lang
        auth.refresh(req).flatMap(Ok(_))
    }

  override val `private`: AuthedRoutes[AuthedUser, F] = AuthedRoutes.of {
    case GET -> Root / "me" as user =>
      Ok(user)

    case ar @ GET -> Root / "logout" as user =>
      auth.destroySession(ar.req, user.email) *> NoContent()
  }
}
