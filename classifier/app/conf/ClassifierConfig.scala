package conf

import play.api.Play.current

object ClassifierConfig {
  val config = current.configuration

  val s3Bucket = config.getRequiredString("s3.bucket")
}
