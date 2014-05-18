package miner.contentapi

import com.gu.openplatform.contentapi
import scala.concurrent.ExecutionContext.Implicits.global
import conf.TrainerConfiguration
import dispatch.Future
import com.gu.openplatform.contentapi.connection.HttpResponse
import grizzled.slf4j.Logging

object Api extends contentapi.FutureAsyncApi with contentapi.connection.DispatchAsyncHttp with Logging {
  override implicit def executionContext = global

  apiKey = TrainerConfiguration.apiKey

  override val targetUrl = TrainerConfiguration.apiEndPoint getOrElse "http://content.guardianapis.com"

  override def GET(urlString: String, headers: Iterable[(String, String)]): Future[HttpResponse] = {
    val future = super.GET(urlString, headers)
    logger.info(s"Getting $urlString")
    future
  }
}
