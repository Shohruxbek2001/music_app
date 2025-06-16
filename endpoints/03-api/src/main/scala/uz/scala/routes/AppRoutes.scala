package uz.scala.routes

import cats.MonadThrow
import cats.effect.Temporal
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps
import fs2.concurrent.Topic
import io.circe.syntax.EncoderOps
import org.http4s.AuthedRoutes
import org.http4s.circe.JsonDecoder
import org.http4s.server.websocket.WebSocketBuilder2
import org.typelevel.log4cats.Logger

import uz.scala.Language
import uz.scala.algebras.OrdersAlgebra
import uz.scala.domain.events.AppEvent
import uz.scala.domain.events.AppEvent.AddOrderItemData
import uz.scala.domain.events.AppEvent.AppEventKind
import uz.scala.effects.Calendar
import uz.scala.http4s.syntax.all.http4SyntaxReqOps
import uz.scala.http4s.syntax.http4s.http4SyntaxWSOps
import uz.scala.http4s.utils.Routes
import uz.scala.syntax.all.circeSyntaxJsonDecoderOps

final case class AppRoutes[F[_]: JsonDecoder: Temporal: MonadThrow: Calendar](
    orders: OrdersAlgebra[F],
    appTopic: Topic[F, AppEvent],
  )(implicit
    wsb: WebSocketBuilder2[F],
    logger: Logger[F],
    webTopic: Topic[F, AppEvent],
  ) extends Routes[F, Unit] {
  override val path = "/ws"

  private def handleEvent(event: AppEvent)(implicit lang: Language): F[Unit] =
    for {
      _ <- logger.info(s"Received event: ${event.asJson}")
      _ <- event.kind match {
        case AppEventKind.AddOrderItem =>
          event.data.decodeAsF[F, AddOrderItemData]
        case _ =>
          logger.error(s"Unknown event: ${event.asJson}")
      }
    } yield ()

  override val `private`: AuthedRoutes[Unit, F] = AuthedRoutes.of {
    case ar @ GET -> Root / "app" as _ =>
      implicit val language: Language = ar.req.lang
      wsb.createChannel[AppEvent](
        appTopic.subscribeUnbounded,
        event => handleEvent(event),
      )

    case ar @ GET -> Root / "web" as _ =>
      implicit val language: Language = ar.req.lang
      wsb.createChannel[AppEvent](
        webTopic.subscribeUnbounded,
        event => handleEvent(event),
      )
  }
}
