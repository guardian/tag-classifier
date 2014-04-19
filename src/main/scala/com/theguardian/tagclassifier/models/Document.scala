package com.theguardian.tagclassifier.models

import com.gu.openplatform.contentapi.model.Content
import org.jsoup.Jsoup
import com.theguardian.tagclassifier.{StopWords, Lemmatizer}
import com.theguardian.tagclassifier.util.Seqs._

object Document {
  def fromContent(content: Content) = {
    val words = content.safeFields.get("body") map { body =>
      val bodyText = Jsoup.parse(body).text()
      val words = Lemmatizer.lemmatize(bodyText)
      words.filterNot(StopWords.stopWords.contains)
    } getOrElse {
      throw new RuntimeException(
        "Cannot train data set on items of content for which the body was not requested. Is your query correct?"
      )
    }

    Document(content.id, words, content.tags.map(_.id).toSet)
  }
}

case class Document(id: String, words: List[String], tagIds: Set[String]) {
  lazy val tagStats = TagStats(
    1,
    words.length,
    words.frequencies
  )
}
