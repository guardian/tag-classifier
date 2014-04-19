package com.theguardian.tagclassifier.models

import scalaz.Lens
import scalaz.std.map._
import scalaz.syntax.monoid._

object DataSet {
  def empty = DataSet(0, Map.empty, Map.empty)

  val totalArticlesLens = Lens.lensu[DataSet, Int](
    (dataSet, total) => dataSet.copy(totalArticles = total),
    _.totalArticles
  )

  val tagStatsLens = Lens.lensu[DataSet, Map[String, TagStats]](
    (dataSet, stats) => dataSet.copy(tagStats = stats),
    _.tagStats
  )

  val wordStatsLens = Lens.lensu[DataSet, Map[String, WordStats]](
    (dataSet, stats) => dataSet.copy(wordStats = stats),
    _.wordStats
  )
}

case class DataSet(
  totalArticles: Int,
  tagStats: Map[String, TagStats],
  wordStats: Map[String, WordStats]
) {

  def addTagStats(tagId: String, stats: TagStats) = {
    DataSet.tagStatsLens.mod(_ |+| Map(tagId -> stats), this)
  }

  def addWordStats(word: String, stats: WordStats) = {
    DataSet.wordStatsLens.mod(_ |+| Map(word -> stats), this)
  }
}