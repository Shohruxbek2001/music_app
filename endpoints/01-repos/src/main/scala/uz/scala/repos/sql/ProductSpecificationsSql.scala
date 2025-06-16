package uz.scala.repos.sql

import doobie._
import doobie.implicits._

import uz.scala.domain.ProductId
import uz.scala.domain.SpecificationId
import uz.scala.doobie.Sql
import uz.scala.doobie.syntax.all._
import uz.scala.repos.dto

private[repos] object ProductSpecificationsSql extends Sql[dto.ProductSpecification] {
  val insert: Update[dto.ProductSpecification] = Update[dto.ProductSpecification](
    sql"""INSERT INTO $table ($columns) VALUES ($values)""".internals.sql
  )

  def delete(
      productId: ProductId,
      specificationId: SpecificationId,
    ): Update0 =
    sql"""DELETE FROM $table WHERE id = $productId AND $specificationId""".update
}
