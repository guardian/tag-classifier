package com.theguardian.tagclassifier.models

import scalaz.syntax.monoid._
import org.specs2.mutable.Specification

class WordStatsSpec extends Specification {
  "|+|" should {
    "append two word stats together" in {
      WordStats(Set("profile/charliebrooker", "football/england")) |+|
        WordStats(Set("profile/charliebrooker", "sport/football")) mustEqual WordStats(Set(
        "profile/charliebrooker",
        "football/england",
        "sport/football"
      ))
    }
  }
}
