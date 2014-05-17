package conf

import play.api.Play.current

object TagClassifierConfiguration {
  private val conf = current.configuration

  val apiKey = conf.getString("content_api.key")

  val apiEndPoint = conf.getString("content_api.host")

  val s3Bucket = conf.getRequiredString("s3.bucket")
}
