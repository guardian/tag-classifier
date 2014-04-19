package com.theguardian.tagclassifier

import com.theguardian.tagclassifier.models.{WordStats, Document, DataSet}
import grizzled.slf4j.Logging
import com.theguardian.tagclassifier.util.StopWatch

object Trainer extends Logging {
  def train(items: Seq[Document]) = {
    val totalTime = new StopWatch

    val dataSet = items.foldLeft(DataSet.empty) { (dataSet, content) =>
      //val documentTime = new StopWatch
      val withTagStats = content.tagIds.foldLeft(dataSet) { case (tagStats, tag) =>
        tagStats.addTagStats(tag, content.tagStats)
      }

      val newSet = content.words.distinct.foldLeft(withTagStats) { case (tagStats, word) =>
        tagStats.addWordStats(word, WordStats(content.tagIds))
      }
      //println(s"Added document in ${documentTime.elapsed}ms")

      newSet.incrementTotalArticles
    }

    logger.info(s"Trained data set in ${totalTime.elapsed}ms")

    dataSet
  }
}
