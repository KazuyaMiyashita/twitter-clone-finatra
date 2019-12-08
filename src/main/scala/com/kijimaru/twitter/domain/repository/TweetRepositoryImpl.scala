package com.kijimaru.twitter.domain.repository

import javax.inject.Inject
import com.kijimaru.twitter.domain.entity.{Follow, Tweet}
import com.kijimaru.twitter.module.DBModule.DBContext

import java.time.ZonedDateTime

class TweetRepositoryImpl @Inject()(ctx: DBContext) extends TweetRepository {

  import TweetRepositoryImpl._
  import ctx._

  override def findById(id: Long): Option[TweetRecord] = run {
    quote {
      query[Tweet].filter(_.id == lift(id))
    }
  }.headOption

  override def findTimeline(userId:  Long): Either[String, List[Tweet]] = run {
    quote {
      for {
        followeeIds <- query[Follow].withFilter(_.userId == lift(userId)).map(_.followed)
        tweets <- query[Tweet].filter(tweet => liftQuery(followeeIds).contains(tweet.userId))
      } yield (followeeIds, tweets)
    }
  }
}

object TweetRepositoryImpl {

  case class TweetRecord(
    id: Long,
    user_id: String,
    text: String,
    created_at: ZonedDateTime
  )

}
