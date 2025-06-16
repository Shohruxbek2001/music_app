package uz.scala.routes

import cats.MonadThrow
import cats.implicits.catsSyntaxOptionId
import cats.implicits.toFlatMapOps
import io.estatico.newtype.ops.toCoercibleIdOps
import org.http4s.AuthedRoutes
import org.http4s.circe.JsonDecoder

import uz.scala.Language
import uz.scala.ObjectId
import uz.scala.SuccessResult
import uz.scala.algebras.MarketsAlgebra
import uz.scala.algebras.OrdersAlgebra
import uz.scala.algebras.ProductsAlgebra
import uz.scala.domain.AuthedUser
import uz.scala.domain.MarketId
import uz.scala.domain.enums.Privilege
import uz.scala.domain.markets.MarketInput
import uz.scala.domain.users.Role
import uz.scala.effects.Calendar
import uz.scala.http4s.syntax.all.deriveEntityEncoder
import uz.scala.http4s.syntax.all.http4SyntaxReqOps
import uz.scala.http4s.utils.Routes
import uz.scala.shared.ResponseMessages.MARKET_CREATED
import uz.scala.shared.ResponseMessages.MARKET_DELETED
import uz.scala.shared.ResponseMessages.MARKET_UPDATED
import uz.scala.shared.ResponseMessages.NO_ACCESS_TO_MARKET
import uz.scala.syntax.all.BooleanOps
import uz.scala.syntax.all.coercibleEncoder
import uz.scala.syntax.all.validationOps
import uz.scala.validation.Rules
import uz.scala.validation.createRule

final case class MarketRoutes[F[_]: JsonDecoder: MonadThrow: Calendar](
    products: ProductsAlgebra[F],
    markets: MarketsAlgebra[F],
    orders: OrdersAlgebra[F],
  ) extends Routes[F, AuthedUser] {
  override val path = "/markets"

  override val `private`: AuthedRoutes[AuthedUser, F] = AuthedRoutes.of {
    case ar @ POST -> Root as user =>
      implicit val language: Language = ar.req.lang
      implicit val role: Role = user.role
      authorize[F](Privilege.CreateMarket) {
        ar.req.decodeR[MarketInput] { input =>
          markets
            .create(input)
            .flatMap(id => Created(ObjectId(id, MARKET_CREATED(language).some)))
        }
      }

    case ar @ GET -> Root as user =>
      implicit val role: Role = user.role
      implicit val language: Language = ar.req.lang
      authorize[F](Privilege.ViewMarkets) {
        markets.get.flatMap(Ok(_))
      }

    case ar @ DELETE -> Root / UUIDVar(marketId) as user =>
      implicit val role: Role = user.role
      implicit val language: Language = ar.req.lang
      authorize[F](Privilege.DeleteMarket) {
        markets
          .delete(marketId.coerce[MarketId])
          .flatMap(_ => Created(SuccessResult(MARKET_DELETED(language))))
      }

    case ar @ PUT -> Root / UUIDVar(marketId) as user =>
      implicit val language: Language = ar.req.lang
      implicit val role: Role = user.role
      authorize[F](Privilege.UpdateMarket) {
        ar.req.decodeR[MarketInput] { input =>
          markets
            .update(marketId.coerce[MarketId], input)
            .flatMap(_ => Created(SuccessResult(MARKET_UPDATED(language))))
        }
      }

    case ar @ GET -> Root / UUIDVar(marketId) as user =>
      implicit val role: Role = user.role
      implicit val language: Language = ar.req.lang
      authorize[F](Privilege.ViewOwnMarket, Privilege.ViewMarkets) {
        implicit val rules: Rules[Role] = List(
          createRule[Role](NO_ACCESS_TO_MARKET(language))(role =>
            Boolean.or(
              role.privileges.contains(Privilege.ViewMarkets),
              Boolean.and(
                role.privileges.contains(Privilege.ViewOwnMarket),
                user.market.exists(_.id == marketId.coerce[MarketId]),
              ),
            )
          )
        )
        role.validate[F].flatMap(_ => markets.findById(marketId.coerce[MarketId]).flatMap(Ok(_)))
      }

  }
}
