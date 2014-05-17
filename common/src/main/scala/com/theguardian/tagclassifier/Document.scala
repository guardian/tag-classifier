package com.theguardian.tagclassifier

import org.jsoup.Jsoup

object Document {
  def fromContent(isInClass: Boolean)(body: String) = {
    Document(extractFeatures(body), isInClass)
  }

  def extractFeatures(body: String) = Lemmatizer.lemmatize(Jsoup.parse(body).text())
    .filterNot(StopWords.smart.contains)
}

case class Document(features: List[String], isInClass: Boolean)

