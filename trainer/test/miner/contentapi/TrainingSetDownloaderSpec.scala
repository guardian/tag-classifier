package miner.contentapi

import org.specs2.mutable.Specification
import play.api.test._
import play.api.test.Helpers._

class TrainingSetDownloaderSpec extends Specification {
  "containingTag" should {
    "return n documents with bodies that have all been tagged with the given tag" in {
      running(FakeApplication()) {
        forall(TrainingSetDownloader.containingTag("tone/comment", 51).toBlockingObservable.toList) { content =>
          (content.body must beSome.which(!_.isEmpty)) and (content.tags.map(_.id) must contain("tone/comment"))
        }
      }
    }

    "return an empty observable for a non-existent tag" in {
      running(FakeApplication()) {
        TrainingSetDownloader.containingTag("doing/it/for/teh/lulz", 51).toBlockingObservable.toList must be(Nil)
      }
    }
  }

  "notContainingTag" should {
    "return n documents with bodies, none of which have been tagged with the given tag" in {
      running(FakeApplication()) {
        forall(TrainingSetDownloader.notContainingTag("tone/comment", 51).toBlockingObservable.toList) { content =>
          (content.body must beSome.which(!_.isEmpty)) and (content.tags.map(_.id) must not contain "tone/comment")
        }
      }
    }
  }
}
