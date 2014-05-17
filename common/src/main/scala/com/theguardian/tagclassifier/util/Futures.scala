package com.theguardian.tagclassifier.util

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object Futures {
  implicit class RichFutureCompanion(companion: Future.type) {
    def fromTry[A](t: Try[A]): Future[A] = t match {
      case Success(a) => Future.successful(a)
      case Failure(error) => Future.failed(error)
    }
  }
}
