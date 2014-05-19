package miner

import com.gu.openplatform.contentapi.model.{SearchResponse, Content}
import scala.concurrent.{Promise, Future}
import scala.concurrent.duration.FiniteDuration
import akka.actor.ActorSystem
import scala.concurrent.ExecutionContext.Implicits.global

package object contentapi {
  implicit class RichContent(content: Content) {
    def body = content.safeFields.get("body")

    def bodyOrDie = body getOrElse {
      throw new RuntimeException("Expected Content to have body, but had none. Is your query correct?")
    }
  }

  implicit class RichSearchResponse(searchResponse: SearchResponse) {
    def isLastPage = searchResponse.currentPage == searchResponse.pages
  }

  implicit class RichFutureCompanion(companion: Future.type) {
    /** Future of Unit that completes after the given delay */
    def delayed(delay: FiniteDuration)(implicit system: ActorSystem) = {
      val promise = Promise[Unit]()

      system.scheduler.scheduleOnce(delay) {
        promise.success(())
      }

      promise.future
    }
  }
}
