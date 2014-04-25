package com.theguardian.tagclassifier

import org.specs2.mutable.Specification

class TrainingSetBuilderSpec extends Specification {
  "wordCounts" should {
    "correctly calculate frequencies of words" in {
      TrainingSetBuilder.wordCounts(Seq(
        Document(List("hello", "world"), false),
        Document(List("hello", "is", "it", "me", "you're", "looking", "for"), false),
        Document(List("world", "weary"), true)
      ).par) mustEqual Map(
        "for" -> 1,
        "hello" -> 2,
        "is" -> 1,
        "it" -> 1,
        "me" -> 1,
        "looking" -> 1,
        "weary" -> 1,
        "world" -> 2,
        "you're" -> 1
      )
    }
  }

  "buildDataSet" should {
    val fixture = TrainingSetBuilder.buildDataSet(Seq(
      Document(List("it's", "a", "whole", "new", "world"), false),
      Document(List("it's", "show", "down", "time"), false),
      Document(List("time", "and", "time", "again"), false),
      Document(List("it's", "a", "damn", "shame"), true),
      Document(List("frankly", "my", "dear", "I", "don't", "give", "a", "damn"), true)
    ).par)

    "discard words that occur fewer than 3 times" in {
      (fixture.columns must contain("a")) and
        (fixture.columns must contain("it's")) and
        (fixture.columns must not contain "damn") and
        (fixture.columns must not contain "show")
    }

    "list feature columns in alphabetical order" in {
      fixture.columns.init.sorted mustEqual fixture.columns.init
    }

    "have isInClass false for documents belonging to the negative set" in {
      forall(fixture.rows.take(3).map(_.isInClass)) { _ must beFalse }
    }

    "have 1 in the final column for documents belonging to the positive set" in {
      forall(fixture.rows.drop(3).map(_.isInClass)) { _ must beTrue }
    }

    "correctly tally frequencies across the documents" in {
      def lookUp(column: String, row: Row) =
        fixture.columns.indexOf(column) match {
          case -1 => None
          case idx => row.features.lift(idx)
        }

      val firstDoc = fixture.rows.head

      lookUp("it's", firstDoc) mustEqual Some(1)
      lookUp("a", firstDoc) mustEqual Some(1)
      lookUp("whole", firstDoc) mustEqual None
      lookUp("time", firstDoc) mustEqual Some(0)

      val thirdDoc = fixture.rows(2)

      lookUp("time", thirdDoc) mustEqual Some(2)
    }
  }
}
