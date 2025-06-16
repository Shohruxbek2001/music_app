package uz.scala

import _root_.doobie.ConnectionIO

import uz.scala.repos._

case class Repositories[F[_]](
    users: UsersRepository[F],
    roles: RolesRepository[F],
    products: ProductsRepository[F],
    specifications: SpecificationsRepository[F],
    categories: CategoriesRepository[F],
    markets: MarketsRepository[F],
    orders: OrdersRepository[F],
  )

object Repositories {
  def make: Repositories[ConnectionIO] =
    Repositories(
      users = UsersRepository.make,
      roles = RolesRepository.make,
      products = ProductsRepository.make,
      specifications = SpecificationsRepository.make,
      categories = CategoriesRepository.make,
      markets = MarketsRepository.make,
      orders = OrdersRepository.make,
    )
}
