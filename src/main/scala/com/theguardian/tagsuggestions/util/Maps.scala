package com.theguardian.tagsuggestions.util

import scalaz.syntax.monoid._
import scalaz.Monoid

object Maps {
  /** Using the given Monoid of B to combine values when the same key is present in both, merges left and right maps */
  def merge[A, B](left: Map[A, B], right: Map[A, B])(implicit monoid: Monoid[B]): Map[A, B] =
    (for {
      k <- left.keySet ++ right.keySet
      v1 = left.getOrElse(k, ∅)
      v2 = right.getOrElse(k, ∅)
    } yield k -> (v1 |+| v2)).toMap
}
