package com.theguardian.tagclassifier.models

import scalaz.{Monoid, Lens}
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

  implicit val dataSetMonoid = new Monoid[DataSet] {
    override def zero: DataSet = empty

    override def append(f1: DataSet, f2: => DataSet): DataSet = {
      /** unionWith is faster if the smaller Map is passed as the first argument */
      val smaller :: larger :: Nil = List(f1, f2).sortBy(_.totalArticles)

      DataSet(
        f1.totalArticles + f2.totalArticles,
        unionWith(smaller.tagStats, larger.tagStats)(_ |+| _),
        unionWith(smaller.wordStats, larger.wordStats)(_ |+| _)
      )
    }
  }
}

case class DataSet(
  totalArticles: Int,
  tagStats: Map[String, TagStats],
  wordStats: Map[String, WordStats]
)