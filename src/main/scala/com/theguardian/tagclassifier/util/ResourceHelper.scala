package com.theguardian.tagclassifier.util

trait ResourceHelper {
  protected def slurp(path: String) =
    Option(getClass.getClassLoader.getResource(path)).map(scala.io.Source.fromURL(_).mkString)

  protected def slurpOrDie(path: String) = slurp(path) getOrElse {
    throw new RuntimeException(s"Unable to slurp required file $path")
  }
}
