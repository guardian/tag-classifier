import play.api.Configuration

package object conf {
  implicit class RichConfiguration(config: Configuration) {
    def getRequiredString(key: String) = config.getString(key) getOrElse {
      throw new RuntimeException(s"Required property $key not in config")
    }
  }
}
