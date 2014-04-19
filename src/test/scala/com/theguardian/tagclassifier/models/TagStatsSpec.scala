package com.theguardian.tagclassifier.models

import scalaz.syntax.monoid._
import org.specs2.mutable.Specification

class TagStatsSpec extends Specification {
  "|+|" should {
    "add tag stats together correctly" in {
      TagStats(5, 10, Map(
        "hello" -> 5,
        "world" -> 5
      )) |+| TagStats(1, 4, Map(
        "hello" -> 1,
        "who" -> 1,
        "is" -> 1,
        "it" -> 1
      )) mustEqual TagStats(
        6,
        14,
        Map(
          "hello" -> 6,
          "world" -> 5,
          "who" -> 1,
          "is" -> 1,
          "it" -> 1
        )
      )
    }
  }
}
