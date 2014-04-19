package com.theguardian.tagclassifier

import com.gu.openplatform.contentapi.model.Content
import com.theguardian.tagclassifier.models.{WordStats, TagStats, DataSet}
import util.Seqs._
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist

object Trainer {
  implicit class RichContent(content: Content) {
    lazy val words = content.safeFields.get("body") map { body =>
      Lemmatizer.lemmatize(Jsoup.clean(body, Whitelist.none()))
    } getOrElse {
      throw new RuntimeException(
        "Cannot train data set on items of content for which the body was not requested. Is your query correct?"
      )
    }

    lazy val wordFrequencies = words.frequencies

    lazy val tagStats = TagStats(
      1,
      words.length,
      wordFrequencies
    )

    lazy val wordStats = WordStats(content.tags.toSet)
  }


  def train(items: Seq[Content]) = {
    items.foldLeft(DataSet.empty) { (dataSet, content) =>
      val richContent = new RichContent(content)

      val withTagStats = content.tags.foldLeft(dataSet) { case (tagStats, tag) =>
        tagStats.addTagStats(tag, richContent.tagStats)
      }

      content.words.distinct.foldLeft(withTagStats) { case (tagStats, word) =>
        tagStats.addWordStats(word, richContent.wordStats)
      }
    }
  }

}
