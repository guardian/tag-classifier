package com.theguardian.tagsuggestions

import util.Maps.merge
import scalaz.std.AllInstances.doubleInstance

case class Triple(word1: String, word2: String, word3: String)

case class Tag(tag: String)

case class TripleStats(
  triple: Triple,
  occurrences: Int,
  tagOccurrences: Seq[(Tag, Int)]
)

object Inference {
  /** Given a list of stats sequences for the triples in a piece of text, returns a sorted list of tags, and the
    * prediction score
    *
    * @param statsSeq The statistics
    * @return The sorted list of tags with prediction scores
    */
  def suggestions(
    statsSeq: Seq[TripleStats]
  ): Seq[(Tag, Double)] =
    statsSeq map { case TripleStats(triple, totalOccurrences, tagOccurrences) =>
      tagOccurrences map { case (tag, occurrences) => tag -> occurrences.toDouble / totalOccurrences }
    } reduce { (occurrences1, occurrences2) =>
      merge(occurrences1.toMap, occurrences2.toMap).toSeq
    } map {
      case (tag, pSum) => tag -> pSum / statsSeq.length
    } sortBy { -_._2 }
}
