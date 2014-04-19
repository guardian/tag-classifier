package com.theguardian.tagclassifier

import org.specs2.mutable.Specification

class LemmatizerSpec extends Specification {
  "Lemmatizer" should {
    "lemmatize a document" in {
      Lemmatizer.lemmatize(
        """
          |Armed pro-Russian separatists seized more buildings in eastern Ukraine earlier in the day, expanding
          |their control after the government failed to follow through on a threatened military crackdown.
          |
          |In a call on Monday night that the White House said Moscow requested, the US president told Putin that
          |those forces were threatening to undermine and destabilise the government in Kiev.
        """.stripMargin) shouldEqual List(
        "arm", "pro-russian", "separatist", "seize", "more", "building", "in", "eastern", "Ukraine", "earlier",
        "in", "the", "day", "expand", "they", "control", "after", "the", "government", "fail", "to", "follow",
        "through", "on", "a", "threaten", "military", "crackdown", "in", "a", "call", "on", "Monday", "night",
        "that", "the", "White", "House", "say", "Moscow", "request", "the", "US", "president", "tell", "Putin",
        "that", "those", "force", "be", "threaten", "to", "undermine", "and", "destabilise", "the", "government",
        "in", "Kiev"
      )
    }
  }
}
