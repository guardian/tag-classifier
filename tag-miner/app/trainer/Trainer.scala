package trainer

import de.bwaldvogel.liblinear._
import miner.DataSet
import grizzled.slf4j.Logging
import com.theguardian.tagclassifier.util.StopWatch

object Trainer extends Logging {
  /** Train a model from the given data set */
  def train(
      dataSet: DataSet,
      cost: Double = 1.0,
      epsilon: Double = 0.001,
      solverType: SolverType = SolverType.L1R_LR
  ) = {
    val problem = new Problem()

    problem.n = dataSet.columns.length
    problem.l = dataSet.rows.length
    problem.y = (dataSet.rows map { row =>
      if (row._2) {
        1.0
      } else {
        0.0
      }
    }).toArray
    /** Confused as to why Scala was complaining about this anyway, given FeatureNode implements Feature */
    problem.x = dataSet.rows.map(_._1.toArray.asInstanceOf[Array[Feature]]).toArray

    val parameter = new Parameter(solverType, cost, epsilon)

    val stopWatch = new StopWatch
    val model = Linear.train(problem, parameter)

    logger.info(s"Trained model $model in ${stopWatch.elapsed}ms")

    model
  }
}
