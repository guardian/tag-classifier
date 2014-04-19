package com.theguardian.tagclassifier.contentapi

import scala.concurrent.duration._
import rx.lang.scala.Observable
import grizzled.slf4j.Logging
import concurrent.ExecutionContext.Implicits.global

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

  /** Downloads roughly n documents from Content API. (Give or take a few, as it will just skip over errors.) */
  def download(n: Int) =
    Observable.interval(DelayBetweenQueries).drop(1).take(Math.ceil(n.toDouble / PageSize).toInt) flatMap { page =>
      Observable.from(query.page(page.toInt).response.map({ response =>
        /** It appears that not all items of content have a body for some reason */
        Observable.from(response.results.filter(_.safeFields.contains("body")))
      })).flatten onErrorResumeNext { error =>
        logger.error(s"Error downloading page $page", error)
        Observable.empty
      }
    }
}
