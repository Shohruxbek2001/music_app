package uz.scala.http

import cats.effect.Async
import fs2.concurrent.Topic
import org.http4s.server

import uz.scala.Algebras
import uz.scala.domain.AuthedUser
import uz.scala.domain.events.AppEvent
import uz.scala.http4s.HttpServerConfig

case class Environment[F[_]: Async](
    config: HttpServerConfig,
    middleware: server.AuthMiddleware[F, AuthedUser],
    appMiddleware: server.AuthMiddleware[F, Unit],
    algebras: Algebras[F],
    appTopic: Topic[F, AppEvent],
    webTopic: Topic[F, AppEvent],
  )
