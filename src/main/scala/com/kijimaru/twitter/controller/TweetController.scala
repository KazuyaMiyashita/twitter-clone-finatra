package com.kijimaru.twitter.controller

import Dao.{LikeDao, ProfileDao, RetweetDao, TweetDao}
import com.kijimaru.twitter.servise.TweetService
import com.twitter.finagle.http.{Request, RequestProxy}
import com.twitter.finatra.http.Controller
import javax.inject.{Inject, Singleton}

@Singleton
class TweetController @Inject() (
  profileDao: ProfileDao,
  tweetService: TweetService,
  likeDao: LikeDao,
  retweetDao: RetweetDao,
  tweetDao: TweetDao
) extends Controller {

  //TODO receive content as file(image,text,markdown)
  post("/tweet/new") { request: Request =>
    tweetService.tweet(
      request.getParam("user_id").toLong,
      request.getParam("text"),
      Option(request.getParams("content").toString)
    )
  }

  post("/tweet/:tweetId/like") { request: Request =>
    val tweetId = request.getParam("tweetId").toLong
    val userId = request.getParam("user_id").toLong
    likeDao.like(userId, tweetId)
    profileDao.updateLikeCount(userId, 1)
  }

  post("/tweet/:tweetId/retweet") { request: Request =>
    val tweetId = request.getParam("tweetId").toLong
    val userId = request.getParam("user_id").toLong
    retweetDao.retweet(tweetId, userId)
    profileDao.updateLikeCount(userId, 1)
  }

  post("/tweet/seed") { request: Request =>
    tweetDao.seed()
  }
}
