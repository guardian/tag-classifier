package com.theguardian.tagclassifier.models

/** CAPI tag */
case class Tag(tagType: String, id: String)

/** Statistics about articles that have been classified with this tag
  *
  * @param tag The tag
  * @param totalArticles The total number of articles that have been classified with this tag
  * @param totalWords The sum of the length of all the articles that have been classified with this tag
  * @param occurrencesOfWord Occurrences of given words in articles that have been classified with this tag
  */
case class TagStats(
  tag: Tag,
  totalArticles: Int,
  totalWords: Int,
  occurrencesOfWord: Map[String, Int]
)