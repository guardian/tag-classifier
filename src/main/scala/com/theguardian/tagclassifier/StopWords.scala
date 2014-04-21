package com.theguardian.tagclassifier

import com.theguardian.tagclassifier.models.Document
import scalaz.std.map._

object StopWords {
  val stopWords = Set(
    "a", "an", "and", "are", "as", "at", "be", "but", "by",
    "for", "if", "in", "into", "is", "it",
    "no", "not", "of", "on", "or", "such",
    "that", "the", "their", "then", "there", "these",
    "they", "this", "to", "was", "will", "with"
  )

  /** A descending list of words and the frequencies with which they occur in the document set
    *
    * (Useful for generating a stop words set - just choose those which are extremely frequent.)
    */
  def fromDocuments(documents: Seq[Document]) =
    documents.view.flatMap(_.words).foldLeft(Map.empty[String, Int]) { case (acc, word) =>
      insertWith(acc, word, 1)(_ + _)
    }.toSeq.sortBy(-_._2)
}
