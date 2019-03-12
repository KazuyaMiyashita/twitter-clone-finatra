import com.twitter.finagle.Http
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.stats.NullStatsReceiver
import com.twitter.finagle.tracing.NullTracer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.finatra.http.{Controller, HttpServer}
import Config._
import _root_.Controller.{TimelineController, TweetSearchController, UserManageController, UserTweetController}

object Main extends TwitterCloneServer

class TwitterCloneServer extends HttpServer {

  // 色々な設定項目はBaseHttpServerを参照
  override def defaultHttpPort: String = conf.getString("port")

  override def defaultHttpServerName: String = conf.getString("host")

  override def configureHttpServer(server: Http.Server): Http.Server = {
    server
      .withCompressionLevel(0)
      .withStatsReceiver(NullStatsReceiver)
      .withTracer(NullTracer)
  }

  override def configureHttp(router: HttpRouter): Unit = {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[CorsFilter]
      .filter[CommonFilters]
      .add[UserManageController]
      .add[TweetSearchController]
      .add[TimelineController]
      .add[UserTweetController]
  }
}

class HomeController extends Controller {
  private[this] val helloWorldResponseText = "Hello, World!"

  get("/json") { request: Request =>
    Map("message" -> "Hello, World!")
  }

  get("/plaintext") { request: Request =>
    helloWorldResponseText
  }

}
