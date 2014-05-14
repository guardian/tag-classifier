import grizzled.slf4j.Logging
import miner.TrainingSetBuilder
import play.api.Application
import play.api.mvc.WithFilters
import trainer.{Tester, Trainer}

object Global extends WithFilters() with Logging {
  override def onStart(app: Application): Unit = {
    super.onStart(app)

    val dataSet = TrainingSetBuilder.build("tone/comment", 10000)

    val (trainingSet, testingSet) = dataSet.partition(0.8)

    val model = Trainer.train(trainingSet)

    val results = Tester.test(model)(testingSet)

    logger.info(s"Correctly categorized ${results.totalCorrect}/${results.dataSetSize} ()")
  }
}
