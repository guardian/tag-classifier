package com.theguardian.tagclassifier

import com.theguardian.tagclassifier.models.{WordStats, Document, DataSet}
import grizzled.slf4j.Logging

object Trainer extends Logging {
  def train(items: Seq[Document]) = {
    val start = System.currentTimeMillis
    def elapsed = System.currentTimeMillis - start

    val dataSet = items.foldLeft(DataSet.empty) { (dataSet, content) =>
      val withTagStats = content.tagIds.foldLeft(dataSet) { case (tagStats, tag) =>
        tagStats.addTagStats(tag, content.tagStats)
      }

      content.words.distinct.foldLeft(withTagStats) { case (tagStats, word) =>
        tagStats.addWordStats(word, WordStats(content.tagIds))
      }
    }

    logger.info(s"Trained data set in ${elapsed}ms")

    dataSet
  }
}
