package com.kijimaru.twitter.domain.repository

import javax.inject.Inject
import com.kijimaru.twitter.domain.entity.{Follow, Tweet}
import com.kijimaru.twitter.domain.master.ContentType
import com.kijimaru.twitter.module.DBModule.DBContext
import java.time.LocalDateTime

import com.kijimaru.twitter.domain.repository.TweetRepository.CreateTweetRequest

import scala.util.Try

class TweetRepositoryImpl @Inject()(ctx: DBContext) extends TweetRepository {

  import TweetRepositoryImpl._
  import ctx._

  override def create(request: CreateTweetRequest): Try[Long] = Try {
    run(
      quote {
        query[TweetRecord]
          .insert(
            _.userId -> lift(request.userId),
            _.text -> lift(request.text),
            _.contentUrl -> lift(request.contentUrl)
          )
          .returningGenerated(_.id)
      }
    )
  }

  override def findById(id: Long): Option[Tweet] = {
    val record: Option[(TweetRecord, ProfileRecord)] = run {
      quote {
        querySchema[TweetRecord]("tweets")
          .filter(_.id == lift(id))
          .join(querySchema[ProfileRecord]("profile"))
            .on((t, p) => t.userId == p.userId)
      }
    }.headOption
    record.map(r => createTweetEntity(r._1, r._2))
  }

  override def findByUser(userId: Long, offset: Int): Option[Tweet] = ???

  override def findByFollow(userId: Long, offset: Int): List[Tweet] = {
    val record: List[(TweetRecord, ProfileRecord)] = run {
      quote {
        (for {
          follow <- query[Follow].withFilter(_.userId == lift(userId))
          tweet <- querySchema[TweetRecord]("tweets")
            .join(_.userId == follow.followed)
          profile <- querySchema[ProfileRecord]("profile")
            .join(_.userId == tweet.userId)
        } yield (tweet, profile))
          .sortBy(_._1.id)
          .drop(lift(offset))
          .take(100)
      }
    }
    record.map(r => createTweetEntity(r._1, r._2))
  }

  override def findTimeline(userId: Long): Try[List[Tweet]] = ???

  override def like(id: Long): Either[String, Unit] = ???

  override def retweet(id: Long): Either[String, Unit] = ???

  override def seed(): Unit = ???
}

object TweetRepositoryImpl {

  case class TweetRecord(
    id: Long,
    userId: Long,
    text: String,
    contentUrl: String,
    createdAt: LocalDateTime
  )

  case class ProfileRecord(
    userId: Long,
    icon: String
  )

  def createTweetEntity(
    tweet: TweetRecord,
    profile: ProfileRecord
  ) = Tweet(
    id = tweet.id,
    userId = tweet.userId,
    userIcon = profile.icon,
    text = tweet.text,
    contentType = ContentType.Empty, // TODO
    contentUrl = tweet.contentUrl,
    createdAt = tweet.createdAt,
  )

}
