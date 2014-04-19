package com.theguardian.tagclassifier

import com.theguardian.tagclassifier.contentapi.{Api, TrainingSetDownloader}
import Trainer.RichContent

object TagClassifier extends App {
  val TrainingSetSize = 3100

  println(s"Retrieving $TrainingSetSize documents from Content API ...")

  println(s"Credentials -- api key ${Api.apiKey} -- host ${Api.targetUrl}")

  val documents = TrainingSetDownloader.download(TrainingSetSize)

  val dataSet :: Nil = Trainer.train(documents drop 100).toBlockingObservable.toList

  println("Trained ... ")

  val scoreAgainst = documents take 100

  scoreAgainst.toBlockingObservable.toList foreach { document =>

    println(s"Attempting to predict tags for ${document.id}")

    val tagSet = document.tagIds.toSet

    val predictions = Inference.suggestions(document.words, dataSet.tagStats, 3000).map(_._1) take tagSet.size

    val correct = tagSet intersect predictions.toSet

    println(s"\tGot ${correct.size} / ${tagSet.size} correct!")
  }

}
