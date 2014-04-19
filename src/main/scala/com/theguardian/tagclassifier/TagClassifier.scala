package com.theguardian.tagclassifier

import com.theguardian.tagclassifier.contentapi.{Api, TrainingSetDownloader}
import com.theguardian.tagclassifier.models.Document

object TagClassifier extends App {
  val TrainingSetSize = 5000
  val TestingSetSize = 50

  println(s"Retrieving $TrainingSetSize documents from Content API ...")

  println(s"Credentials -- api key ${Api.apiKey} -- host ${Api.targetUrl}")

  val documents = TrainingSetDownloader
    .download(TrainingSetSize + TestingSetSize)
    .map(Document.fromContent)
    .toBlockingObservable
    .toList

  val testSet = documents.take(TestingSetSize)
  val trainSet = documents.drop(TestingSetSize)

  val dataSet = Trainer.train(trainSet)

  println("Trained ... ")

  testSet foreach { document =>

    println(s"Attempting to predict tags for ${document.id}")

    val tagSet = document.tagIds.toSet

    val predictions = Inference.suggestions(
      document.words,
      dataSet.tagStats,
      TrainingSetSize
    ).map(_._1) take tagSet.size

    println("Wanted:")

    tagSet foreach { tag =>
      println("\t" + tag)
    }

    println("Predicted:")

    predictions foreach { tag =>
      println("\t" + tag)
    }

    val correct = tagSet intersect predictions.toSet

    println(s"\tGot ${correct.size} / ${tagSet.size} correct!")
  }
}
