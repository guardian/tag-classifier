package miner.contentapi

import grizzled.slf4j.Logging
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.gu.openplatform.contentapi.model.{Content, SearchResponse}
import play.api.libs.iteratee.{Enumeratee, Enumerator}
import scala.concurrent.duration._
import play.api.Play.current
import play.libs.Akka

object TrainingSetDownloader extends Logging {
  lazy implicit val actorSystem = Akka.system()

  /** 50 is the maximum Content API allows */
  val PageSize = 50

  val MaxRetries = 3

  val PauseBetweenQueries = 50.millis
  val RetryAfter = 200.millis

  val query = Api
    .search
    .showFields("body")
    .showTags("all")
    .pageSize(PageSize)
    .orderBy("newest")

  protected def runQuery(getPage: Int => Future[SearchResponse]): Enumerator[Content] = {
    def getPageWithRetries(page: Int, retries: Int = MaxRetries): Future[SearchResponse] =
      if (retries < 1) getPage(page) else getPage(page) recoverWith {
        case error =>
          logger.error(s"Error downloading page $page", error)
          Future.delayed(RetryAfter).flatMap(_ => getPageWithRetries(page, retries - 1))
      }

    Enumerator.unfoldM[Option[Int], List[Content]](Option(1)) {
      case Some(pageNumber) =>
        getPageWithRetries(pageNumber) map { searchResponse =>
          val nextPage = if (searchResponse.isLastPage) None else Some(pageNumber + 1)

          Some(nextPage, searchResponse.results)
        }

      case None => Future.successful(None)
    }.flatMap(Enumerator.apply) through Enumeratee.filter(_.body.isDefined)
  }

  /** Observable of n documents from content api that have been tagged with the given tag ID */
  def containingTag(tagId: String, n: Int) = runQuery { page =>
    query.tag(tagId).page(page).response
  } through Enumeratee.take(n)

  /** Observable of n documents from content api that have not been tagged with the given tag ID */
  def notContainingTag(tagId: String, n: Int) = runQuery { page =>
    query.tag(s"-$tagId").page(page).response
  } through Enumeratee.take(n)
}
