package trainer

import grizzled.slf4j.Logging
import de.bwaldvogel.liblinear.{Linear, Model}
import miner.DataSet

case class TestResults(dataSetSize: Int, totalCorrect: Int, percentageCorrect: Double)

object Tester extends Logging {
  def test(model: Model)(dataSet: DataSet) = {
    val correct = dataSet.rows count { case (features, shouldClassify) =>
      (Linear.predict(model, features.toArray) == 1.0) == shouldClassify
    }

    val dataSetSize = dataSet.rows.length

    TestResults(dataSetSize, correct, correct.toDouble / dataSetSize)
  }
}
