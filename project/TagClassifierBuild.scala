import sbt._
import Keys._
import com.gu.deploy.MagentaArtifact._
import sbtassembly.Plugin._
import AssemblyKeys._
import play.Project._
import Dependencies._

object TagClassifierBuild extends Build {
  val projectVersion = "0.1"

  def commonPlayProject(name: String, execName: String) = 
    play.Project(name, projectVersion, path=file(name))
      .settings(magentaArtifactSettings: _*)
      .settings(
        executableName := execName,
        resolvers ++= Seq(
          "Guardian GitHub Releases" at "http://guardian.github.com/maven/repo-releases",
          "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
        ),
        testOptions in Test := Nil,
        mergeStrategy in assembly <<= (mergeStrategy in assembly) {
          (old) => {
            case PathList("scalax", xs@_*) => MergeStrategy.first
            case PathList("play", xs@_*) => MergeStrategy.first
            case PathList("org", "apache", "commons", "logging", xs@_*) => MergeStrategy.first
            case PathList("org", "hamcrest", xs@_*) => MergeStrategy.first
            case PathList("scala", "concurrent", "stm", xs @ _*) => MergeStrategy.first
            case x => old(x)
          }
        }
    )

  val commonDependencies = Seq(
    awsSdk,
    contentApiClient,
    commonsIo,
    slf4j,
    specs2,
    jSoup,
    coreNlp,
    coreNlpModels,
    libLinear,
    playJson
  )

  val common = Project("common", file("common"))
    .settings(
      libraryDependencies ++= commonDependencies
    )

  val trainer = commonPlayProject("trainer", "trainer")
    .settings(
      libraryDependencies ++= Seq(
        rxScala
      ),
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(common)

  val classifier = commonPlayProject("classifier", "classifier")
    .settings(
      libraryDependencies ++= Seq(
        akkaAgent
      ),
      libraryDependencies ++= commonDependencies
    ).dependsOn(common)

  val root = Project("tag-classifier", file("."))
    .aggregate(trainer, classifier, common)
}
