package com.kijimaru.twitter.domain.repository

import com.kijimaru.twitter.domain.entity.Tweet
import com.kijimaru.twitter.domain.master.ContentType

import scala.util.Try

trait TweetRepository {

  import TweetRepository._

  def create(request: CreateTweetRequest): Try[Long]

  def findById(id: Long): Option[Tweet]

  def findByUser(userId: Long, offset: Int): Option[Tweet]

  def findByFollow(userId: Long, offset: Int): List[Tweet]

  def findTimeline(userId: Long): Try[List[Tweet]]

  def like(id: Long): Either[String, Unit]

  def retweet(id: Long): Either[String, Unit]

  def seed(): Unit

}

object TweetRepository {

  case class CreateTweetRequest(
    userId: Long,
    text: String,
    contentType: ContentType,
    contentUrl: String,
  )
}
