package com.theguardian.tagclassifier.conf

import com.gu.conf.ConfigurationFactory

object TagClassifierConfiguration {
  private val conf = ConfigurationFactory.getConfiguration("tag-classifier")

  val apiKey = conf.getStringProperty("content_api.api_key")

  val apiEndPoint = conf.getStringProperty("content_api.end_point")
}
