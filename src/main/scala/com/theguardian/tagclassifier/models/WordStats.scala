package com.theguardian.tagclassifier.models

/** We use this so we can look up a list of possible classes (tags) for which to calculate probabilities for a given
  * document
  *
  * @param word The word
  * @param tagsSeen Tags that documents that have contained that word have been tagged with
  */
case class WordStats(word: String, tagsSeen: Set[Tag])
