package uz.scala.repos.sql

import doobie._
import doobie.implicits._
import doobie.postgres.circe.json.implicits._
import doobie.refined.implicits._
import doobie.util.fragments.whereAndOpt

import uz.scala.domain.ProductId
import uz.scala.domain.products.ProductFilters
import uz.scala.doobie.Sql
import uz.scala.doobie.syntax.all._
import uz.scala.repos.dto

object ProductsSql extends Sql[dto.Product] {
  val insert: Update[dto.Product] =
    Update[dto.Product](
      sql"""INSERT INTO $table ($columns) VALUES ($values) ON CONFLICT (name, market_id) DO UPDATE
              SET stock_quantity = $table.stock_quantity + EXCLUDED.stock_quantity,
                price = EXCLUDED.price,
                discount_price = EXCLUDED.discount_price,
                description = EXCLUDED.description
            """
        .internals
        .sql
    )

  def findById(id: ProductId): Query0[dto.Product] =
    sql"""SELECT $columns FROM $table WHERE id = $id AND deleted_at IS NULL LIMIT 1"""
      .query[dto.Product]

  def get(filters: ProductFilters): Query0[(dto.ProductDetails, Long)] = {
    val filtersList = List(
      filters.sku.map(sku => fr"sku = $sku"),
      filters.name.map(_.value + "%").map(name => fr"name ILIKE $name"),
      filters.barcode.map(barcode => fr"barcode = $barcode"),
      filters.categoryId.map(categoryId => fr"category_id = $categoryId"),
      filters.marketId.map(marketId => fr"market_id = $marketId"),
      filters.productId.map(productId => fr"product_id = $productId"),
    ).flatten
    sql"""SELECT
            $columns,
            incoming_products,
            market_products,
            disposed_products,
            specifications,
            COUNT(*) OVER() AS total
          FROM product_details_view
          ${whereAndOpt(filtersList)}
        """
      .paginateOpt(filters.limit, filters.page)
      .query[(dto.ProductDetails, Long)]
  }

  val nextSkuNumber: Query0[Int] =
    sql"SELECT nextval('products_sku_seq')".query[Int]

  def update(product: dto.Product): Update0 =
    sql"""UPDATE $table
        SET name = ${product.name},
            slug = ${product.slug},
            price = ${product.price},
            discount_price = ${product.discountPrice},
            description = ${product.description},
            stock_quantity = ${product.stockQuantity},
            category_id = ${product.categoryId},
            updated_at = ${product.updatedAt}
        WHERE id = ${product.id}""".update

  def delete(id: ProductId): Update0 =
    sql"""UPDATE $table SET deleted_at = NOW() WHERE id = $id""".update

}
