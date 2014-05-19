import com.theguardian.tagclassifier.util.StopWatch
import com.theguardian.tagclassifier.data.{TestingInfo, ModelInfo}
import data.TrainerS3Storage
import grizzled.slf4j.Logging
import miner.TrainingSetBuilder
import play.api.{Play, Application}
import play.api.mvc.WithFilters
import scala.util.{Failure, Success}
import trainer.{Tester, Trainer}
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global

object Global extends WithFilters() with Logging {
  val dataSetSize = 10000
  val testTag = "tone/matchreports"

  override def onStart(app: Application): Unit = {
    super.onStart(app)

    if (!Play.isTest) {
      for {
        (dataSet, scalingVector) <- TrainingSetBuilder.build(testTag, dataSetSize)
      } {
        val (trainingSet, testingSet) = dataSet.partition(0.8)

        val model = Trainer.train(trainingSet)

        val results = Tester.test(model)(testingSet)

        logger.info(s"Correctly categorized ${results.totalCorrect}/${results.dataSetSize} ()")

        logger.info("Uploading to S3 ... ")

        val stopWatch = new StopWatch

        TrainerS3Storage.storage.storeModel(ModelInfo(
          testTag,
          dataSet.columns,
          scalingVector.toList,
          model,
          TestingInfo(results.dataSetSize, results.totalCorrect
          ))) match {
          case Success(_) =>
            logger.info(s"Uploaded to S3 in $stopWatch")
          case Failure(error) =>
            logger.error(s"Unable to upload to S3 after $stopWatch", error)
        }
      }
    }
  }
}
