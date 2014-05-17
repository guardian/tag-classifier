package com.theguardian.tagclassifier.models

object FeatureRange {
  def zero = FeatureRange(0, 0)
}

/** Represents the minimum and maximum values seen for a given feature, i.e. the minimum times a word occurs in a given
  * document in the document set (which would normally be zero) and the maximum.
  *
  * @param min The minimum occurrences
  * @param max The maximum occurrences
  */
case class FeatureRange(min: Int, max: Int) {
  def union(other: FeatureRange) = FeatureRange(
    other.min min min,
    other.max max max
  )

  def scale(x: Int) = (x - min).toDouble / (max - min)
}