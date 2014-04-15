

organization := "com.theguardian"

name := "tag-classifier"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "reactivemongo" % "0.10.0",
  "org.clapper" %% "grizzled-slf4j" % "1.0.1",
  "com.gu.openplatform" %% "content-api-client" % "2.11",
  "org.specs2" %% "specs2" % "2.2.3",
  "org.jsoup" % "jsoup" % "1.7.3",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.3.1",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.3.1" classifier "models"
)

