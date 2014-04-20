package com.theguardian.tagclassifier

import com.theguardian.tagclassifier.models.TagStats
import grizzled.slf4j.Logging

object Inference extends Logging {
  /** Uses Laplacian correction, so that words that do not occur do not unduly distort the model */
  def probabilityOfWord(tagStats: TagStats, word: String, numberOfUniqueWordsInD: Int) = {
    val cw = tagStats.occurrencesOfWord.getOrElse(word, 0)
    val v = numberOfUniqueWordsInD

    (1 + cw).toDouble / (tagStats.totalWords + v)
  }

  def probabilityOfDocument(tagStats: TagStats, features: Seq[String], totalNumberOfArticles: Int) = {
    val numberUniqueFeatures = features.distinct.length
    val probabilityOfTag = tagStats.totalArticles / totalNumberOfArticles.toDouble

    /** Use logs to prevent underflow issues */
    (Seq(probabilityOfTag) ++ features.map(probabilityOfWord(tagStats, _, numberUniqueFeatures))).map(Math.log).sum
  }

  /**
    * Given the features of a document, and a set of classes against which to classify it, returns a descending list
    * of most likely tags, with their scores.
    *
    * @param features The features of the document (i.e., a lemmatized set of words, with stop words removed)
    * @param classes The classes against which to match (i.e., tags, with the appropriate data for calculating
   *                 probabilities of seeing given words in documents that have been tagged).
    * @param totalNumberOfArticles Total number of articles the data set has been trained on.
    * @return List of predicted tags in descending order of prediction score.
    */
  def suggestions(
    features: Seq[String],
    classes: Map[String, TagStats],
    totalNumberOfArticles: Int
  ): Seq[(String, Double)] =
    (classes map { case (tag, tagStats) =>
      tag -> probabilityOfDocument(tagStats, features, totalNumberOfArticles)
    }).toSeq.sortBy(-_._2)
}
