package com.theguardian.tagclassifier

import com.gu.openplatform.contentapi.model.Content
import com.theguardian.tagclassifier.models.{WordStats, Tag, TagStats}
import util.Seqs._
import scalaz.Lens

object DataSet {
  def empty = DataSet(0, Map.empty, Map.empty)

  val totalArticlesLens = Lens.lensu[DataSet, Int](
    (dataSet, total) => dataSet.copy(totalArticles = total),
    _.totalArticles
  )

  val tagStatsLens = Lens.lensu[DataSet, Map[Tag, TagStats]](
    (dataSet, stats) => dataSet.copy(tagStats = stats),
    _.tagStats
  )

  val wordStatsLens = Lens.lensu[DataSet, Map[String, WordStats]](
    (dataSet, stats) => dataSet.copy(wordStats = stats),
    _.wordStats
  )

  //def tagTagStatsLens(tag: Tag) = tagStatsLens compose Lens.MapLens
}

case class DataSet(
  totalArticles: Int,
  tagStats: Map[Tag, TagStats],
  wordStats: Map[String, WordStats]
) {
  /*
  def addWordStats(word: String, stats: WordStats) = {
    val WordStats(word, newTags) = stats
    val allTags = wordStats.get(word).map(_.tagsSeen).getOrElse(Set.empty) ++ newTags
    DataSet.wordStatsLens.mod(_ + (word -> WordStats(word, allTags)), this)
  }

  def addTagStats(stats: TagStats) = {

    val TagStats(tag, newArticles, newWords, newWordMap) = stats

    DataSet.tagStatsLens.mod(_ + tag -> TagStats(tag, ), this)
  }*/
}

object Trainer {

  def train(items: Seq[Content]) = {
    val dataSet = DataSet.empty
    val item = items(0)

    val features = Lemmatizer.lemmatize(item.safeFields.getOrElse("body", throw new RuntimeException(
      "Cannot train data set on items of content for which the body was not requested. Is your query correct?"
    )))

    val featureSet = features.toSet
    val featureFrequencies = features.frequencies



  }

}
