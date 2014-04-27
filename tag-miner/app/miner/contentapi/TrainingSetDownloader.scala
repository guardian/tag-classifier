package miner.contentapi

import scala.concurrent.duration._
import rx.lang.scala.Observable
import grizzled.slf4j.Logging
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.gu.openplatform.contentapi.model.{Content, SearchResponse}

object TrainingSetDownloader extends Logging {
  /** 50 is the maximum Content API allows */
  val PageSize = 50

  /** So as not to overload Content API */
  val DelayBetweenQueries = 1.second

  val query = Api
    .search
    .showFields("body")
    .showTags("all")
    .pageSize(PageSize)
    .orderBy("newest")

  protected def runQuery(items: Int)(getPage: Int => Future[SearchResponse]): Observable[Content] =
    (Observable.interval(DelayBetweenQueries).drop(1) flatMap { page =>
      Observable.from(getPage(page.toInt)) onErrorResumeNext { error =>
       /** We don't really care if we miss out a page, just continue */
        logger.error(s"Error downloading page $page", error)
        Observable.empty
      }
    }).takeWhile(!_.isLastPageOrBeyond).flatMap({ response =>
      /** It appears that not all items of content have a body for some reason */
      Observable.from(response.results.filter(_.body.isDefined))
    }).take(items)

  /** Observable of n documents from content api that have been tagged with the given tag ID */
  def containingTag(tagId: String, n: Int) = runQuery(n) { page =>
    query.tag(tagId).page(page).response
  }

  /** Observable of n documents from content api that have not been tagged with the given tag ID */
  def notContainingTag(tagId: String, n: Int) = runQuery(n) { page =>
    query.tag(s"-$tagId").page(page).response
  }
}
