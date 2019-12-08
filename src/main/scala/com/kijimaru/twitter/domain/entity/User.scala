package com.kijimaru.twitter.domain.entity

import java.util.UUID

case class User(
  id: User.UserId,
  screenName: User.ScreenName,
  email: User.Email
)

object User {

  case class UserId(value: UUID) extends AnyVal
  object UserId {
    def of(str: String): Either[String, UserId] = {
      try {
        Right(UserId(UUID.fromString(str)))
      } catch {
        case e: IllegalArgumentException => Left(e.toString)
      }
    }
  }

  case class ScreenName(value: String)
  object ScreenName {
    val MaxLength = 32
    def of(str: String): Either[String, ScreenName] = {
      if (str.length <= MaxLength) Right(ScreenName(str))
      else Left("ScreenName length must be less than 32 characters")
    }
  }



  case class Email(value: String)
  object Email {
    val MaxLength = 255
    val ValidEmail = """/^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/"""
    def of(str: String): Either[String, Email] = {
      if (str.length > MaxLength) Left("ScreenName length must be less than 255 characters")
      else if (!str.matches(ValidEmail)) Left("E-mail address format is invalid")
      else Right(Email(str))
    }
  }


  def of(
    _id: String,
    _screenName: String,
    _email: String,
  ): Either[String, User] = {
    for {
      id <- UserId.of(_id)
      screenName <- ScreenName.of(_screenName)
      email <- Email.of(_email)
    } yield User(id, screenName, email)
  }


}
