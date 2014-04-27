package com.theguardian.tagclassifier

import org.specs2.mutable.Specification
import miner.LibSvmFormatter

class LibSvmFormatterSpec extends Specification {
  "format" should {
    "create a stream of strings representing the data set as formatted for libsvm" in {
      LibSvmFormatter.format(DataSet(
        List("foo", "bar"),
        Seq(
          Row(true, Seq(20, 0, 4)),
          Row(true, Seq(2, 3, 0)),
          Row(false, Seq(0, 10, 10))
        )
      )).toList mustEqual List(
        "+1 1:20 3:4",
        "+1 1:2 2:3",
        "-1 2:10 3:10"
      )
    }
  }
}
