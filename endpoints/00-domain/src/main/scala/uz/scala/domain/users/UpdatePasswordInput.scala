package uz.scala.domain.users

import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.JsonCodec
import io.circe.refined._

@JsonCodec
final case class UpdatePasswordInput(
    currentPassword: NonEmptyString,
    newPassword: NonEmptyString,
  )
