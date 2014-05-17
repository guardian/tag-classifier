package classifier

import com.theguardian.tagclassifier.data.ModelInfo
import com.theguardian.tagclassifier.util.Seqs._
import de.bwaldvogel.liblinear.{FeatureNode, Linear}

case class Classifier(private val modelInfo: ModelInfo) {
  lazy val rangeByFeature = (modelInfo.features zip modelInfo.ranges).toMap

  def classify(features: List[String]) = {
    val featureFrequencies = features.frequencies

    val featureNodes = modelInfo.features.zipWithIndex map { case (feature, idx) =>
      val frequencyOfFeature = featureFrequencies.getOrElse(feature, 0)

      new FeatureNode(idx + 1, rangeByFeature(feature).scale(frequencyOfFeature))
    }

    Linear.predict(modelInfo.model, featureNodes.toArray)
  }
}
