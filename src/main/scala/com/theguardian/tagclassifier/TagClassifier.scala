package com.theguardian.tagclassifier

import com.theguardian.tagclassifier.contentapi.{Api, TrainingSetDownloader}
import com.theguardian.tagclassifier.models.{WordStats, Document}

object TagClassifier extends App {
  val TrainingSetSize = 1000
  val TestingSetSize = 50

  println(s"Retrieving $TrainingSetSize documents from Content API ...")

  println(s"Credentials -- api key ${Api.apiKey} -- host ${Api.targetUrl}")

  val documents = TrainingSetDownloader
    .download(TrainingSetSize + TestingSetSize)
    .map(Document.fromContent)
    .filter(!_.words.isEmpty)
    .toBlockingObservable
    .toList

  val testSet = documents.take(TestingSetSize)
  val trainSet = documents.drop(TestingSetSize)

  val dataSet = Trainer.train(trainSet)

  println("Trained ... ")

  testSet foreach { document =>

    println(s"Attempting to predict tags for ${document.id}")

    val tagSet = document.tagIds.toSet

    // TODO this is what word stats was for ...
    val tagsToConsider = (document.words.distinct flatMap { word =>
      dataSet.wordStats.getOrElse(word, WordStats.empty).tagsSeen
    }).toSet

    val allPredictions = Inference.suggestions(
      document.words,
      dataSet.tagStats,
      TrainingSetSize
    )

    println("Predicted:")

    allPredictions.take(tagSet.size) foreach { case (tag, probability) =>
      println("\t%10f %10s".format(probability, tag))
    }

    val predictions = allPredictions.map(_._1) take tagSet.size

    println("Wanted:")

    tagSet foreach { tag =>
      println("\t" + tag)
    }

    val correct = tagSet intersect predictions.toSet

    println(s"\tGot ${correct.size} / ${tagSet.size} correct!")
  }
}
