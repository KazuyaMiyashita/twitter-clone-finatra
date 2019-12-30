package com.kijimaru.twitter.domain.repository

import io.getquill._
import org.scalatest._
import com.kijimaru.twitter.domain.repository.TweetRepository.CreateTweetRequest
import com.kijimaru.twitter.domain.master.ContentType
import scala.util.Try
import scala.util.Failure

class TweetRepositoryImplSpec extends FunSuite with Matchers with TryValues with BeforeAndAfter {

  lazy val ctx = new MysqlJdbcContext(SnakeCase, "ctx")
  lazy val tweetRepository: TweetRepository = new TweetRepositoryImpl(ctx)

  before {
    import ctx.{run => crun, _}
    // ctx.executeAction("delete `tweet`;")
    // ctx.executeAction("alter table `tweet` auto_increment = 1;")
  }

  after {

  }

  test("create") {

    val request = CreateTweetRequest(
      userId = 42,
      text = "first tweet",
      contentType = ContentType.Empty,
      contentUrl = ""
    )
    val tweetId: Try[Long] = tweetRepository.create(request)

    tweetId match {
      case Failure(exception) => exception.printStackTrace()
      case _ => ()
    }

    tweetId.success.value shouldEqual 1

  }


}
