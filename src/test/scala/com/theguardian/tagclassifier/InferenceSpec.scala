package com.theguardian.tagclassifier

import org.specs2.mutable.Specification
import com.theguardian.tagclassifier.models.TagStats

class InferenceSpec extends Specification {
  val tagStats = TagStats(
    1,
    1,
    Map(
      "xyxxy" -> 1,
      "bonus" -> 2,
      "buzz" -> 1
    )
  )

  "probabilityOfWord" should {
    "be higher for words that occur in the tag stats than those that do not" in {
      Inference.probabilityOfWord(tagStats, "xyxxy", 1) must beGreaterThan(
        Inference.probabilityOfWord(tagStats, "hello", 1)
      )
    }

    "be higher for words that occur more times in the tag stats" in {
      Inference.probabilityOfWord(tagStats, "bonus", 1) must beGreaterThan(
        Inference.probabilityOfWord(tagStats, "xyxxy", 1)
      )
    }

    "not be zero for words that do not occur in the tag stats" in {
      Inference.probabilityOfWord(tagStats, "loldongs", 1) must not be equalTo(0)
    }
  }

  "probabilityOfDocument" should {
    "be higher for documents containing words that occur in the tag stats" in {
      Inference.probabilityOfDocument(tagStats, Seq("bonus", "pies"), 1) must beGreaterThan(
        Inference.probabilityOfDocument(tagStats, Seq("hello", "world"), 1)
      )
    }

    "be higher for documents containing words that more frequently occur in the tag stats" in {
      Inference.probabilityOfDocument(tagStats, Seq("bonus", "roach"), 1) must beGreaterThan(
        Inference.probabilityOfDocument(tagStats, Seq("buzz", "roach"), 1)
      )
    }

    "be higher for documents containing multiple words that occur in the tag stats than just one" in {
      Inference.probabilityOfDocument(tagStats, Seq("xyxxy", "buzz"), 1) must beGreaterThan(
        Inference.probabilityOfDocument(tagStats, Seq("laugh", "aloud"), 1)
      )
    }
  }
}
