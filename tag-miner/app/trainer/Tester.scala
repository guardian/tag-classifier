package trainer

import grizzled.slf4j.Logging
import de.bwaldvogel.liblinear.{Linear, Model}
import miner.DataSet
import com.theguardian.tagclassifier.util.StopWatch

case class TestResults(dataSetSize: Int, totalCorrect: Int, percentageCorrect: Double)

object Tester extends Logging {
  def test(model: Model)(dataSet: DataSet) = {
    val stopWatch = new StopWatch

    val dataSetSize = dataSet.rows.length

    val correct = dataSet.rows count { case (features, shouldClassify) =>
      (Linear.predict(model, features.toArray) == 1.0) == shouldClassify
    }

    logger.info(s"Validated in ${stopWatch.elapsed}: $correct / $dataSetSize correct")

    TestResults(dataSetSize, correct, (correct.toDouble / dataSetSize) * 100)
  }
}
