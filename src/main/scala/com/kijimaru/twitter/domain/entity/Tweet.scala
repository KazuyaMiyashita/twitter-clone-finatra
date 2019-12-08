package com.kijimaru.twitter.domain.entity

import java.util.UUID
import java.time.ZonedDateTime

case class Tweet(
  id: Tweet.TweetId,
  userId: Long,
  text: Tweet.TweetText,
  liked: Long,
  retweeted: Long,
  createdAt: ZonedDateTime
)

object Tweet {

  case class TweetId(value: UUID) extends AnyVal
  object TweetId {
    def of(str: String): Either[String, TweetId] = {
      try {
        Right(TweetId(UUID.fromString(str)))
      } catch {
        case e: IllegalArgumentException => Left(e.toString)
      }
    }
  }

  case class TweetText(value: String)
  object TweetText {
    val MaxLength = 255
    def of(str: String): Either[String, TweetText] = {
      if (str.length <= MaxLength) Right(TweetText(str))
      else Left("Tweet length must be less than 255 characters")
    }
  }

  def of(
    _id: String,
    _userId: Long,
    _text: String,
    _liked: Long,
    _retweeted: Long,
    _createdAt: ZonedDateTime
  ): Either[String, Tweet] = {
    for {
      id <- TweetId.of(_id)
      userId <- Right(_userId)
      text <- TweetText.of(_text)
      liked <- Right(_liked)
      retweeted <- Right(_retweeted)
      createdAt <- Right(_createdAt)
    } yield Tweet(id, userId, text, liked, retweeted, createdAt)
  }

}
