package data

import akka.agent.Agent
import scala.collection.parallel.immutable.ParMap
import classifier.Classifier
import com.theguardian.tagclassifier.data.ModelInfo
import play.api.Play.current
import play.api.libs.concurrent.Akka

object Classifiers {
  implicit private val akkaSystem = Akka.system

  val all = Agent[ParMap[String, Classifier]](ParMap.empty)

  def update(modelInfo: ModelInfo) {
    all send { _ + (modelInfo.tagId -> new Classifier(modelInfo)) }
  }
}
