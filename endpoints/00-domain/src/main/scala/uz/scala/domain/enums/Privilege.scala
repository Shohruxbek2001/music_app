package uz.scala.domain.enums

import enumeratum.EnumEntry.Snakecase
import enumeratum._
import io.circe.Json

sealed trait Privilege extends Snakecase {
  val group: String
}
object Privilege extends Enum[Privilege] with CirceEnum[Privilege] with DoobieEnum[Privilege] {
  case object CreateUser extends Privilege {
    override val group: String = "USER"
  }
  case object UpdateUser extends Privilege {
    override val group: String = "USER"
  }
  case object UpdateAnyUser extends Privilege {
    override val group: String = "USER"
  }
  case object ViewUsers extends Privilege {
    override val group: String = "USER"
  }
  case object CreateSuperUser extends Privilege {
    override val group: String = "USER"
  }
  case object DeleteUser extends Privilege {
    override val group: String = "USER"
  }

  // Category
  case object CreateCategory extends Privilege {
    override val group: String = "CATEGORY"
  }
  case object UpdateCategory extends Privilege {
    override val group: String = "CATEGORY"
  }
  case object DeleteCategory extends Privilege {
    override val group: String = "CATEGORY"
  }
  case object ViewCategories extends Privilege {
    override val group: String = "CATEGORY"
  }

  // Market
  case object CreateMarket extends Privilege {
    override val group: String = "MARKET"
  }
  case object UpdateMarket extends Privilege {
    override val group: String = "MARKET"
  }
  case object DeleteMarket extends Privilege {
    override val group: String = "MARKET"
  }
  case object ViewMarkets extends Privilege {
    override val group: String = "MARKET"
  }
  case object ViewOwnMarket extends Privilege {
    override val group: String = "MARKET"
  }

  // Order
  case object CreateOrder extends Privilege {
    override val group: String = "ORDER"
  }
  case object ViewOrders extends Privilege {
    override val group: String = "ORDER"
  }
  case object ViewOwnOrders extends Privilege {
    override val group: String = "ORDER"
  }
  case object ViewOrder extends Privilege {
    override val group: String = "ORDER"
  }

  // OrderItem
  case object CreateOrderItem extends Privilege {
    override val group: String = "ORDER_ITEM"
  }
  case object UpdateOrderItem extends Privilege {
    override val group: String = "ORDER_ITEM"
  }
  case object DeleteOrderItem extends Privilege {
    override val group: String = "ORDER_ITEM"
  }

  // Product
  case object CreateProduct extends Privilege {
    override val group: String = "PRODUCT"
  }
  case object ViewProducts extends Privilege {
    override val group: String = "PRODUCT"
  }
  case object UpdateProduct extends Privilege {
    override val group: String = "PRODUCT"
  }
  case object DeleteProduct extends Privilege {
    override val group: String = "PRODUCT"
  }
  case object DispatchProduct extends Privilege {
    override val group: String = "PRODUCT"
  }
  case object DisposeProduct extends Privilege {
    override val group: String = "PRODUCT"
  }
  case object ViewDisposedProducts extends Privilege {
    override val group: String = "PRODUCT"
  }
  case object ViewMarketProduct extends Privilege {
    override val group: String = "PRODUCT"
  }
  case object ViewMarketProducts extends Privilege {
    override val group: String = "PRODUCT"
  }

  // Partner
  case object CreatePartner extends Privilege {
    override val group: String = "PARTNER"
  }
  case object ViewPartners extends Privilege {
    override val group: String = "PARTNER"
  }
  case object DeletePartner extends Privilege {
    override val group: String = "PARTNER"
  }
  case object UpdatePartner extends Privilege {
    override val group: String = "PARTNER"
  }

  // Role
  case object CreateRole extends Privilege {
    override val group: String = "ROLE"
  }
  case object UpdateRole extends Privilege {
    override val group: String = "ROLE"
  }
  case object DeleteRole extends Privilege {
    override val group: String = "ROLE"
  }
  case object ViewRoles extends Privilege {
    override val group: String = "ROLE"
  }

  // Settings
  case object UpdateSettings extends Privilege {
    override val group: String = "SETTINGS"
  }
  // Specifications
  case object CreateSpecification extends Privilege {
    override val group: String = "SPECIFICATIONS"
  }
  case object ViewSpecifications extends Privilege {
    override val group: String = "SPECIFICATIONS"
  }
  case object UpdateSpecification extends Privilege {
    override val group: String = "SPECIFICATIONS"
  }
  case object DeleteSpecification extends Privilege {
    override val group: String = "SPECIFICATIONS"
  }

  // Assets
  case object CreateAsset extends Privilege {
    override val group: String = "ASSETS"
  }

  case object CreatePayment extends Privilege {
    override val group: String = "PAYMENTS"
  }
  case object ViewPayments extends Privilege {
    override val group: String = "PAYMENTS"
  }

  override def values: IndexedSeq[Privilege] = findValues

  val groupedValues: List[Json] = values.groupBy(_.group).toList.map {
    case groupName -> values =>
      Json.obj(
        "groupName" -> Json.fromString(groupName),
        "values" -> Json.fromValues(
          values
            .toList
            .filterNot(_ == CreateSuperUser)
            .map(value => Json.fromString(value.entryName))
        ),
      )
  }
}
