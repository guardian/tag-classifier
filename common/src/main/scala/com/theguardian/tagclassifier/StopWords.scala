package com.theguardian.tagclassifier

import com.theguardian.tagclassifier.util.ResourceHelper
import util.Strings._

object StopWords extends ResourceHelper {
  /** SMART list of stop words */
  lazy val smart = Lemmatizer.lemmatize(slurpOrDie("english.stop.txt").words.mkString(" ")).toSet
}
