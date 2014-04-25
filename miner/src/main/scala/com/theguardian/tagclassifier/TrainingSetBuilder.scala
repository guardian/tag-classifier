package com.theguardian.tagclassifier

import scala.collection.parallel.ParSeq
import com.gu.openplatform.contentapi.model.Content
import contentapi._
import util.Seqs._
import scalaz.std.map._

object Document {
  def fromContent(isInClass: Boolean)(content: Content) = {
    Document(Lemmatizer.lemmatize(content.body getOrElse {
      throw new RuntimeException("Cannot lemmatize document without a body")
    }).filterNot(StopWords.smart.contains), isInClass)
  }
}

case class Document(features: List[String], isInClass: Boolean)

case class Row(
  isInClass: Boolean,
  features: Seq[Int]
)

case class DataSet(
  columns: List[String],
  rows: Seq[Row]
)

object TrainingSetBuilder {
  /** Minimum number of times a feature occurs in total for it to be worth considering */
  val MinimumOccurrences = 3

  def wordCounts(items: ParSeq[Document]) =
    items.map(_.features.frequencies).reduce(unionWith(_, _)(_ + _))

  def buildDataSet(documents: ParSeq[Document]) = {
    val wordFrequencies = wordCounts(documents)

    val features = wordFrequencies.toSeq.filter(_._2 >= MinimumOccurrences).map(_._1).sorted

    val rows = documents map { document =>
      val documentFreqs = document.features.frequencies
      Seq(Row(document.isInClass, features.map(documentFreqs.getOrElse(_, 0))))
    } reduce { _ ++ _ }

    DataSet(features.toList, rows)
  }

  def build(tagId: String, dataSetSize: Int) = {
    val withTag = TrainingSetDownloader.containingTag(tagId, dataSetSize / 2).map(Document.fromContent(true))
    val withoutTag = TrainingSetDownloader.notContainingTag(tagId, dataSetSize).map(Document.fromContent(false))

    buildDataSet((withTag ++ withoutTag).take(dataSetSize)
      .toBlockingObservable
      .toList
      .par)
  }
}
