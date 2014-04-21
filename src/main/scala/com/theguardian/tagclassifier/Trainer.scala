package com.theguardian.tagclassifier

import com.theguardian.tagclassifier.models.{Document, DataSet}
import grizzled.slf4j.Logging
import com.theguardian.tagclassifier.util.StopWatch
import scalaz.syntax.monoid._

object Trainer extends Logging {
  def train(items: Seq[Document]) = {
    val totalTime = new StopWatch

    val dataSet = items.par map { content =>
      DataSet(
        1,
        (content.tagIds map { _ -> content.tagStats} ).toMap
      )
    } reduce { _ |+| _ }

    println(s"Trained data set in %.2fs".format(totalTime.elapsed.toDouble / 1000))

    dataSet
  }
}
