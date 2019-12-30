package com.kijimaru.twitter.domain.repository

import com.kijimaru.twitter.domain.entity.User
import scala.util.Try

trait UserRepository {

  import UserRepository._

  def create(form: CreateUserRequest): Try[Long]

  def findById(userId: Long): Option[User]

  def findByEmail(email: String): Option[User]

  def findByToken(token: String): Option[User]

  def getAll(): List[User]

  def update(request: UpdateUserRequest): Try[Unit]

  def authenticate(email: String, rawPassword: String): Either[String, Boolean]

  def setToken(userId: Long, token: String): Try[Unit]

}

object UserRepository {

  case class CreateUserRequest(
    email: String,
    screenName: String,
    hashedPassword: String
  )

  case class UpdateUserRequest(
    id: Long,
    email: String,
    screenName: String,
    hashedPassword: String,
  )
}
