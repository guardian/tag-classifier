package com.theguardian.tagclassifier.util

object Maps {
  /** Merges left and right, appending values that already exist with append */
  def merge[A, B](left: Map[A, B], right: Map[A, B])(append: (B, B) => B): Map[A, B] = {
    right.toSeq.foldLeft(left) {
      case (accumulator, (key, b2)) =>
        accumulator + (key -> accumulator.get(key).map(b1 => append(b1, b2)).getOrElse(b2))
    }
  }
}
