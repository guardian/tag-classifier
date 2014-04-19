package com.theguardian.tagclassifier

import org.specs2.mutable.Specification
import rx.lang.scala.Observable
import com.gu.openplatform.contentapi.model.{Tag, Content}
import org.joda.time.DateTime
import com.theguardian.tagclassifier.utils.ResourceHelper

class TrainerSpec extends Specification with ResourceHelper {
  object Content {
    val empty = new Content(
      "",
      None,
      None,
      new DateTime(),
      "",
      "",
      "",
      None,
      elements = None
    )
  }

  "Trainer" should {
    "train a single item correctly" in {
      val tag = Tag(
        "business/executive-pay-bonuses",
        "keyword",
        webTitle = "Executive Pay Bonuses",
        webUrl = "http://www.theguardian.com/business/executive-pay-bonuses",
        apiUrl = "http://beta.content.guardianapis.com/business/executive-pay-bonuses"
      )

      val Some(dataSet) = Trainer.train(Observable.items(Content.empty.copy(
        fields = Some(Map("body" -> slurpOrDie("executive-pay-ceos-dont-need-cash.html"))),
        tags = List(
          tag
        )
      ))).toBlockingObservable.toList.headOption

      dataSet.tagStats.size mustEqual 1

      val Some(stats) = dataSet.tagStats.get("business/executive-pay-bonuses")

      stats.totalWords mustEqual 921
    }
  }
}
