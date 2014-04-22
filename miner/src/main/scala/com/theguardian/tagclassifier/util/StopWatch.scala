package com.theguardian.tagclassifier.util

class StopWatch {
  val startTime = System.currentTimeMillis

  def elapsed = System.currentTimeMillis - startTime
}
