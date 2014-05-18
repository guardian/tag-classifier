package controllers

import play.api.mvc.{Action, Controller}
import miner.contentapi.Api
import com.theguardian.tagclassifier.util.Futures._
import scala.concurrent.Future
import grizzled.slf4j.Logging
import scala.concurrent.ExecutionContext.Implicits.global
import data.TrainerS3Storage

object HealthCheck extends Controller with Logging {
  def healthCheck = Action.async { request =>
    val contentApiFuture = Api.item.itemId("/").response
    val s3Future = Future.fromTry(TrainerS3Storage.storage.accessControlList)

    (for {
      contentApiResponse <- contentApiFuture
      /** TODO actually check the grants on the ACL to see that the app has permission to put and get? */
      s3Response <- s3Future
    } yield Ok("Everything's ok here!")) recover {
      case error =>
        logger.error("Error in health check", error)
        ServiceUnavailable(s"Error in back ends: ${error.getMessage}")
    }
  }
}
