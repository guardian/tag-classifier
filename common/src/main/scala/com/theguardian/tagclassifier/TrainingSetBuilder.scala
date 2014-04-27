package com.theguardian.tagclassifier

import org.jsoup.Jsoup

object Document {
  def fromContent(isInClass: Boolean)(body: String) = {
    val features = Lemmatizer.lemmatize(Jsoup.parse(body).text())
      .filterNot(StopWords.smart.contains)
    Document(features, isInClass)
  }
}

case class Document(features: List[String], isInClass: Boolean)

