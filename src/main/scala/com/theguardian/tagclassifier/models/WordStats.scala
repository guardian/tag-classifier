package com.theguardian.tagclassifier.models

import scalaz.Monoid
import com.gu.openplatform.contentapi.model.Tag

object WordStats {
  implicit val wordStatsMonoid = new Monoid[WordStats] {
    override def zero: WordStats = WordStats(Set.empty)

    override def append(f1: WordStats, f2: => WordStats): WordStats =
      WordStats(f1.tagsSeen ++ f2.tagsSeen)
  }
}

/** We use this so we can look up a list of possible classes (tags) for which to calculate probabilities for a given
  * document
  *
  * @param tagsSeen Tags that documents that have contained that word have been tagged with
  */
case class WordStats(tagsSeen: Set[Tag])
