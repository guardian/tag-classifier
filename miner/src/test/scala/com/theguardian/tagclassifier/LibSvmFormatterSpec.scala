package com.theguardian.tagclassifier

import org.specs2.mutable.Specification

class LibSvmFormatterSpec extends Specification {
  "format" should {
    "create a stream of strings representing the data set as formatted for libsvm" in {
      LibSvmFormatter.format(DataSet(
        List("foo", "bar"),
        Seq(
          Row(true, Seq(20, 4)),
          Row(true, Seq(2, 3)),
          Row(false, Seq(10, 10))
        )
      )).toList mustEqual List(
        "+1 1:20 2:4",
        "+1 1:2 2:3",
        "-1 1:10 2:10"
      )
    }
  }
}
