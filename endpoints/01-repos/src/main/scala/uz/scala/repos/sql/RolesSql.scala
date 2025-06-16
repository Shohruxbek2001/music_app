package uz.scala.repos.sql

import cats.data.NonEmptyList
import doobie._
import doobie.implicits._
import doobie.refined.implicits._
import doobie.util.fragments._

import uz.scala.domain.RoleId
import uz.scala.domain.users.Role
import uz.scala.doobie.Sql
import uz.scala.doobie.syntax.all._
import uz.scala.repos.dto

private[repos] object RolesSql extends Sql[dto.Role] {
  val insert: Update[dto.Role] = Update[dto.Role](
    sql"""INSERT INTO $table ($columns) VALUES ($values)""".internals.sql
  )
  def findById(id: RoleId): Query0[Role] =
    sql"""
        SELECT * FROM role_privileges_view WHERE role_id = $id
    """.query[Role]

  def update(role: dto.Role): Update0 =
    sql"""UPDATE $table SET name = ${role.name} WHERE id = ${role.id}""".update

  def get(roleIds: NonEmptyList[RoleId]): Query0[Role] =
    sql"""
        SELECT * FROM role_privileges_view ${whereAnd(in(fr"role_id", roleIds))}
    """.query[Role]

  val insertPrivileges: Update[(String, String)] =
    Update[(String, String)](
      "INSERT INTO privileges VALUES (?, ?) ON CONFLICT DO NOTHING"
    )

  val selectAll: Query0[Role] =
    sql"""SELECT * FROM role_privileges_view WHERE role_name != 'TECH_ADMIN'""".query[Role]

  val addRolePrivileges: Update[(RoleId, String)] =
    Update[(RoleId, String)](
      """INSERT INTO role_privileges VALUES (?, ?) ON CONFLICT DO NOTHING"""
    )

  def removePrivileges(roleId: RoleId): Update0 =
    sql"""DELETE FROM role_privileges WHERE role_id = $roleId""".update

  def delete(roleId: RoleId): Update0 =
    sql"""DELETE FROM $table WHERE id = $roleId""".update
}
