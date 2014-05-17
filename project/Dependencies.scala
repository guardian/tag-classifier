import sbt._

object Dependencies {
  val awsSdk = "com.amazonaws" % "aws-java-sdk" % "1.7.9"
  val commonsIo = "org.apache.commons" % "commons-io" % "1.3.2"
  val contentApiClient = "com.gu.openplatform" %% "content-api-client" % "2.11"
  val coreNlp = "edu.stanford.nlp" % "stanford-corenlp" % "3.3.1"
  val coreNlpModels = "edu.stanford.nlp" % "stanford-corenlp" % "3.3.1" classifier "models"
  val guava = "com.google.guava" % "guava" % "17.0-rc2"
  val jSoup = "org.jsoup" % "jsoup" % "1.7.3"
  val libLinear = "de.bwaldvogel" % "liblinear" % "1.94"
  val pickling = "org.scala-lang" %% "scala-pickling" % "0.8.0"
  val rxScala = "com.netflix.rxjava" % "rxjava-scala" % "0.17.6"
  val slf4j = "org.clapper" %% "grizzled-slf4j" % "1.0.1"
  val specs2 = "org.specs2" %% "specs2" % "2.2.3"
}
