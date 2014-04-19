package com.theguardian.tagclassifier.contentapi

import com.gu.openplatform.contentapi
import scala.concurrent.ExecutionContext.Implicits.global
import com.theguardian.tagclassifier.conf.TagClassifierConfiguration

object Api extends contentapi.FutureAsyncApi with contentapi.connection.DispatchAsyncHttp {
  override implicit def executionContext = global

  apiKey = TagClassifierConfiguration.apiKey

  override val targetUrl = TagClassifierConfiguration.apiEndPoint getOrElse "http://content.guardianapis.com"
}
