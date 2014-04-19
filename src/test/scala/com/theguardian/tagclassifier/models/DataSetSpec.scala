package com.theguardian.tagclassifier.models

import org.specs2.mutable.Specification

class DataSetSpec extends Specification {
  "addTagStats" should {
    "insert tag stats if not previously seen" in {
      val tagId = "football/england"
      val tagStats = TagStats(1, 1024, Map("football" -> 1024))

      DataSet.empty.addTagStats(tagId, tagStats) mustEqual DataSet(
        0,
        Map(
          tagId -> tagStats
        ),
        Map.empty
      )
    }

    "add tag details together if tag stats already exist" in {
      val tagId = "profile/charliebrooker"
      val tagStats = TagStats(1, 500, Map("charlie" -> 251, "brooker" -> 249))

      DataSet.empty.copy(tagStats = Map(
        tagId -> TagStats(1, 5, Map("charlie" -> 3, "chaplin" -> 2))
      )).addTagStats(tagId, tagStats) mustEqual DataSet(
        0,
        Map(
          tagId -> TagStats(2, 505, Map("charlie" -> 254, "brooker" -> 249, "chaplin" -> 2))
        ),
        Map.empty
      )
    }
  }

  "addWordStats" should {
    "insert word stats if not previously seen" in {
      val word = "hello"
      val tags = WordStats(Set("football/england", "profile/charliebrooker"))

      DataSet.empty.addWordStats(word, tags) mustEqual DataSet(
        0,
        Map.empty,
        Map(word -> tags)
      )
    }

    "append word stats if previously seen" in {
      val word = "hello"
      val tags = WordStats(Set("football/england", "profile/charliebrooker"))

      DataSet.empty.copy(wordStats = Map(word -> WordStats(Set("football/england", "football/manchesterunited"))))
        .addWordStats(word, tags) mustEqual DataSet(
        0,
        Map.empty,
        Map(word -> WordStats(Set("football/england", "football/manchesterunited", "profile/charliebrooker")))
      )
    }
  }

  "incrementTotalArticles" should {
    "increment the total articles count" in {
      DataSet.empty.incrementTotalArticles mustEqual DataSet(1, Map.empty, Map.empty)
    }
  }
}
