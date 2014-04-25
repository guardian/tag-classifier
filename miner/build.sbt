

organization := "com.theguardian"

name := "tag-classifier"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.clapper" %% "grizzled-slf4j" % "1.0.1",
  "com.gu.openplatform" %% "content-api-client" % "2.11",
  "com.google.guava" % "guava" % "17.0-rc2",
  "com.gu" %% "configuration" % "3.9",
  "org.specs2" %% "specs2" % "2.2.3",
  "org.jsoup" % "jsoup" % "1.7.3",
  "com.netflix.rxjava" % "rxjava-scala" % "0.17.6",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.3.1",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.3.1" classifier "models",
  "tw.edu.ntu.csie" % "libsvm" % "3.17"
)

fork in run := true

javaOptions in run += "-Xmx8G"
