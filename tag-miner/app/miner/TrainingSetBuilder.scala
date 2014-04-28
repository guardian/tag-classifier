package miner

import scala.collection.parallel.ParSeq
import scalaz.std.map._
import com.theguardian.tagclassifier.util.Seqs._
import com.theguardian.tagclassifier.Document
import miner.contentapi._
import de.bwaldvogel.liblinear.FeatureNode
import grizzled.slf4j.Logging
import com.theguardian.tagclassifier.util.StopWatch

case class Row(
  isInClass: Boolean,
  features: Seq[Int]
)

case class DataSet(
  columns: List[String],
  rows: Seq[(Seq[FeatureNode], Boolean)]
)

object TrainingSetBuilder extends Logging {
  /** Minimum number of times a feature occurs in total for it to be worth considering */
  val MinimumOccurrences = 3

  def wordCounts(items: ParSeq[Document]) =
    items.map(_.features.frequencies).reduce(unionWith(_, _)(_ + _))

  def buildDataSet(documents: ParSeq[Document]) = {
    logger.info(s"Choosing features from ${documents.length} documents")

    val chooseWatch = new StopWatch

    val wordFrequencies = wordCounts(documents)
    val features = wordFrequencies.toSeq.filter(_._2 >= MinimumOccurrences).map(_._1).sorted

    logger.info(s"Chose ${features.length} features that occurred at least $MinimumOccurrences times in the " +
      s"documents in ${chooseWatch.elapsed}ms")
    logger.info(s"Extracting features from documents ...")

    val extractWatch = new StopWatch

    val rows = documents map { document =>
      val documentFreqs = document.features.frequencies

      Seq((features.zipWithIndex flatMap { case (feature, index) =>
        documentFreqs.get(feature) map { frequency =>
          new FeatureNode(index, frequency)
        }
      }, document.isInClass))
    } reduce { _ ++ _ }

    logger.info(s"Extracted features in ${extractWatch.elapsed}ms")

    DataSet(features.toList, rows)
  }

  def build(tagId: String, dataSetSize: Int) = {
    val withTag = TrainingSetDownloader.containingTag(tagId, dataSetSize / 2).map(content =>
      Document.fromContent(true)(content.bodyOrDie)
    )
    val withoutTag = TrainingSetDownloader.notContainingTag(tagId, dataSetSize).map(content =>
      Document.fromContent(false)(content.bodyOrDie)
    )

    buildDataSet((withTag ++ withoutTag)
      .take(dataSetSize)
      .toBlockingObservable
      .toList
      .par)
  }
}