package com.theguardian.tagclassifier

import com.theguardian.tagclassifier.contentapi.{Api, TrainingSetDownloader}
import com.theguardian.tagclassifier.models.{WordStats, Document}
import com.theguardian.tagclassifier.util.StopWatch

object TagClassifier extends App {
  val TrainingSetSize = 20000
  val TestingSetSize = 50

  println(s"Retrieving $TrainingSetSize documents from Content API ...")

  println(s"Credentials -- api key ${Api.apiKey} -- host ${Api.targetUrl}")

  val documents = TrainingSetDownloader
    .download(TrainingSetSize + TestingSetSize)
    .map(Document.fromContent)
    .filter(doc => !doc.words.isEmpty || doc.id.startsWith("crosswords/"))
    .toBlockingObservable
    .toList

  val testSet = documents.take(TestingSetSize)
  val trainSet = documents.drop(TestingSetSize)

  val totalStopWatch = new StopWatch
  /*(1 to 10) foreach { _ =>
    val dataSet = Trainer.train(trainSet)

    println(dataSet.totalArticles) // so the compiler doesn't do optimizations & break my test ...
  }

  println(s"Average time: %.2fs".format((totalStopWatch.elapsed.toDouble / 1000) / 10))
  */

  val dataSet = Trainer.train(trainSet)

  println("Trained ... ")

  // bite me
  var total = 0d

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

    /*println("Predicted:")

    allPredictions.take(tagSet.size) foreach { case (tag, probability) =>
      println("\t%10f %10s".format(probability, tag))
    }*/

    val predictions = allPredictions.map(_._1) take tagSet.size

    /*println("Wanted:")

    tagSet foreach { tag =>
      println("\t" + tag)
    }*/

    val correct = tagSet intersect predictions.toSet

    println(s"\tGot ${correct.size} / ${tagSet.size} correct!")

    total += (correct.size.toDouble / tagSet.size)
  }

  println(s"On average predicted ${(total / testSet.size) * 100}% correctly")

  println(s"Most frequent words seen:")
  StopWords.fromDocuments(trainSet).take(50) foreach { case (word, frequency) =>
    println(s"    %10d %s".format(frequency, word))
  }
}
