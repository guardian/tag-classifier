package com.theguardian.tagclassifier.util

import scala.util.Try

object IntegerString {
  def unapply(integerString: String) = Try {
    Integer.parseInt(integerString)
  }.toOption
}
