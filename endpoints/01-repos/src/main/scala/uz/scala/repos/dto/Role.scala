package uz.scala.repos.dto

import eu.timepit.refined.types.string.NonEmptyString

import uz.scala.domain.RoleId

case class Role(id: RoleId, name: NonEmptyString)
