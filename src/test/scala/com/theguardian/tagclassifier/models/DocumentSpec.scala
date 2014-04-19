package com.theguardian.tagclassifier.models

import org.specs2.mutable.Specification

class DocumentSpec extends Specification {
  "tagStats" should {
    "produce the correct tag stats for a given document" in {
      Document(
        "fakeid",
        List(
          "how",
          "much",
          "wood",
          "would",
          "a",
          "wood",
          "chuck",
          "chuck",
          "if",
          "a",
          "wood",
          "chuck",
          "could",
          "chuck",
          "wood"
        ),
        Set.empty
      ).tagStats mustEqual TagStats(
        1,
        15,
        Map(
          "how" -> 1,
          "much" -> 1,
          "wood" -> 4,
          "would" -> 1,
          "a" -> 2,
          "chuck" -> 4,
          "if" -> 1,
          "could" -> 1
        )
      )

    }
  }
}
