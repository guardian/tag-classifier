import data.Classifiers
import com.amazonaws.services.s3.AmazonS3Client
import com.theguardian.tagclassifier.data.S3Storage
import conf.ClassifierConfig
import grizzled.slf4j.Logging
import play.api._
import scala.util.{Failure, Success}
import play.api.Play.current

object Global extends GlobalSettings with Logging {
  override def onStart(app: Application) {
    if (!Play.isTest) {

      val s3Client = new AmazonS3Client()

      val storage = S3Storage(s3Client, ClassifierConfig.s3Bucket)

      storage.loadModel("tone-comment") match {
        case Success(model) =>
          logger.info("Successfully loaded tone/comment model from S3")
          Classifiers.update(model)

        case Failure(error) =>
          logger.error("Could not load tone/comment model from S3", error)
      }
    }
  }
}
