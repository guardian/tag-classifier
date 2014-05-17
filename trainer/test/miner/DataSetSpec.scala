package miner

import org.specs2.mutable.Specification
import de.bwaldvogel.liblinear.{FeatureNode, Feature}

class DataSetSpec extends Specification {
  "partition" should {
    "throw an exception for a fraction <= 0" in {
      DataSet.empty.partition(0) should throwA[RuntimeException]
    }

    "throw an exception for a fraction >= 1" in {
      DataSet.empty.partition(1) should throwA[RuntimeException]
    }

    val feature1 = new FeatureNode(1, 1.0)
    val feature2 = new FeatureNode(2, 2.0)
    val feature3 = new FeatureNode(3, 3.0)

    val row1 = (Seq(feature1), false)
    val row2 = (Seq(feature2), false)
    val row3 = (Seq(feature3), false)
    val row4 = (Seq(feature1), true)
    val row5 = (Seq(feature2), true)
    val row6 = (Seq(feature3), true)

    val columns = List("hello", "world")

    val dataSet = DataSet(columns, Seq(row1, row2, row3, row4, row5, row6))

    val (left, right) = dataSet.partition(0.67)

    "pass on the columns to its partitioned data sets" in {
      (left.columns mustEqual columns) and (right.columns mustEqual columns)
    }

    "correctly splice the data between the sets" in {
      (left.rows.size mustEqual 4) and (right.rows.size mustEqual 2)
    }

    "correctly proportion negatives and positives in each data set" in {
      (left.rows.count(_._2) mustEqual 2) and (right.rows.count(_._2) mustEqual 1)
    }
  }
}
