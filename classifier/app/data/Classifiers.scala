package data

import akka.agent.Agent
import scala.collection.parallel.immutable.ParMap
import classifier.Classifier

object Classifiers {
  val all = Agent[ParMap[String, Classifier]](ParMap.empty)
}
