package com.theguardian.tagclassifier.util

object Seqs {
  implicit class RichSeq[A](as: Seq[A]) {
    def frequencies = as.groupBy(identity).mapValues(_.length)
  }
}
