package uz.scala.repos.sql

import java.util.UUID

import cats.implicits._
import cats.implicits.catsSyntaxOptionId
import doobie._
import doobie.implicits._
import doobie.postgres.implicits._
import doobie.refined.implicits._
import doobie.util.fragments._

import uz.scala.domain.EmailAddress
import uz.scala.domain.UserId
import uz.scala.doobie.Sql
import uz.scala.doobie.syntax.all._
import uz.scala.repos.dto
import uz.scala.repos.dto.User

private[repos] object UsersSql extends Sql[dto.User] {
  private val TechAdminRoleId: UUID = UUID.fromString("7aa5ba51-5f32-4123-b88c-aca7c8e7b033")
  def findByEmail(email: EmailAddress): Query0[dto.User] =
    sql"""SELECT $columns FROM $table WHERE email = $email AND deleted_at IS NULL LIMIT 1"""
      .query[dto.User]

  def findById(id: UserId): Query0[dto.User] =
    sql"""SELECT $columns FROM $table WHERE id = $id AND deleted_at IS NULL LIMIT 1"""
      .query[dto.User]

  val insert: Update[User] = Update[dto.User](
    sql"""INSERT INTO $table ($columns) VALUES ($values)""".internals.sql
  )

  def delete(id: UserId): Update0 =
    sql"""UPDATE $table SET deleted_at = now() WHERE id = $id""".update

  def update(user: dto.User): Update0 =
    sql"""UPDATE $table
        SET status = ${user.status},
            phone = ${user.phone},
            password = ${user.password},
            role_id = ${user.roleId},
            updated_at = ${user.updatedAt}
        WHERE id = ${user.id}""".update
}
