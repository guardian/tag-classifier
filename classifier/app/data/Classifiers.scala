package data

import akka.agent.Agent
import scala.collection.parallel.immutable.ParMap
import classifier.Classifier
import com.theguardian.tagclassifier.data.ModelInfo
import play.api.Play.current
import play.api.libs.concurrent.Akka
import models.Suggestion
import grizzled.slf4j.Logging

object Classifiers extends Logging {
  implicit private val akkaSystem = Akka.system

  val all = Agent[ParMap[String, Classifier]](ParMap.empty)

  def update(modelInfo: ModelInfo) {
    all send { _ + (modelInfo.tagId -> new Classifier(modelInfo)) }
  }

  def classify(features: List[String]) = {
    logger.info(s"Classifying for features $features")

    all.get().mapValues(classifier => classifier.classify(features) -> classifier.successRate).toList collect {
      case (tagId, (1.0, successRate)) => Suggestion(tagId, successRate)
    }
  }
}
