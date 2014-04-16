package com.theguardian.tagclassifier.models

import scalaz.Monoid
import com.theguardian.tagclassifier.util.Maps

/** CAPI tag */
case class Tag(tagType: String, id: String)

object TagStats {
  implicit val tagStatsMonoid = new Monoid[TagStats] {
    override def zero: TagStats = TagStats(0, 0, Map.empty)

    override def append(f1: TagStats, f2: => TagStats): TagStats = TagStats(
      f1.totalArticles + f2.totalArticles,
      f1.totalWords + f2.totalWords,
      Maps.merge(f1.occurrencesOfWord, f2.occurrencesOfWord)(_ + _)
    )
  }

}

/** Statistics about articles that have been classified with this tag
  *
  * @param totalArticles The total number of articles that have been classified with this tag
  * @param totalWords The sum of the length of all the articles that have been classified with this tag
  * @param occurrencesOfWord Occurrences of given words in articles that have been classified with this tag
  */
case class TagStats(
  totalArticles: Int,
  totalWords: Int,
  occurrencesOfWord: Map[String, Int]
)