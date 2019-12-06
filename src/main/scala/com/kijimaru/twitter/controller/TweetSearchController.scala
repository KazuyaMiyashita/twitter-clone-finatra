package com.kijimaru.twitter.controller

import dao.TweetDao
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import javax.inject.Inject

class TweetSearchController @Inject() (
  tweetDao: TweetDao
) extends Controller {

  get("/tweet/:id") { request: Request =>
    val id = request.getIntParam("id")
    tweetDao.findById(id)
      .getOrElse(s"Tweet id: ${id} not exists")
  }

  get("/tweets/:number") { request: Request =>
    val number = request.getIntParam("number")
    (1 to number).map(
      i => tweetDao.findById(i).getOrElse(s"tweet id:${i} not exists")
    )
  }
}