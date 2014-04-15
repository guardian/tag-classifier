package com.theguardian.tagsuggestions

case class Tag(tag: String)

case class TagStats(
  tag: Tag,
  totalArticles: Int,
  totalWords: Int,
  occurrencesOfWord: Map[String, Int]
) {
  /** Uses Laplacian correction, so that words that do not occur do not unduly distort the model */
  def probabilityOfWord(word: String, numberOfUniqueWordsInD: Int) = {
    val cw = occurrencesOfWord.getOrElse(word, 0)
    val v = numberOfUniqueWordsInD

    (1 + cw).toDouble / (totalWords + v)
  }

  def probabilityOfDocument(features: Seq[String], totalNumberOfArticles: Int) = {
    val numberUniqueFeatures = features.toSet.size
    val probabilityOfTag = totalNumberOfArticles.toDouble / totalArticles

    /** Use logs to prevent underflow issues */
    (Seq(probabilityOfTag) ++ features.map(probabilityOfWord(_, numberUniqueFeatures))).map(Math.log).sum
  }
}

object Inference {
  /**
    * Given the features of a document, and a set of classes against which to classify it, returns a descending list
    * of most likely tags, with their scores.
    *
    * @param features The features of the document (i.e., a lemmatized set of words, with stop words removed)
    * @param classes The classes against which to match (i.e., tags, with the appropriate data for calculating
   *                 probabilities of seeing given words in documents that have been tagged.
    * @param totalNumberOfArticles Total number of articles the data set has been trained on.
    * @return List of predicted tags in descending order of prediction score.
    */
  def suggestions(
    features: Seq[String],
    classes: Set[TagStats],
    totalNumberOfArticles: Int
  ): Seq[(Tag, Double)] =
    (classes map { tagStats =>
      tagStats.tag -> tagStats.probabilityOfDocument(features, totalNumberOfArticles)
    }).toSeq.sortBy(-_._2)
}
