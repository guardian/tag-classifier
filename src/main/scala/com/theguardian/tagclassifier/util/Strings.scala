package com.theguardian.tagclassifier.util

object Strings {
  implicit class RichString(s: String) {
    def words = """(\S+)""".r.findAllIn(s)
  }
}
