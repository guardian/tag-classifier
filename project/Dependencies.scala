import sbt._

object Dependencies {
  val slf4j = "org.clapper" %% "grizzled-slf4j" % "1.0.1"
  val contentApiClient = "com.gu.openplatform" %% "content-api-client" % "2.11"
  val guava = "com.google.guava" % "guava" % "17.0-rc2"
  val guardianConfiguration = "com.gu" %% "configuration" % "3.9"
  val specs2 = "org.specs2" %% "specs2" % "2.2.3"
  val jSoup = "org.jsoup" % "jsoup" % "1.7.3"
  val rxScala = "com.netflix.rxjava" % "rxjava-scala" % "0.17.6"
  val coreNlp = "edu.stanford.nlp" % "stanford-corenlp" % "3.3.1"
  val coreNlpModels = "edu.stanford.nlp" % "stanford-corenlp" % "3.3.1" classifier "models"
  val libLinear = "de.bwaldvogel" % "liblinear" % "1.94"
}
