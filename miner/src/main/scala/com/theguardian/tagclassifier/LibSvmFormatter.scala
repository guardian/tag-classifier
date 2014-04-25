package com.theguardian.tagclassifier

object LibSvmFormatter {
  def format(dataSet: DataSet) =
    dataSet.rows.toStream map { row =>
      val classLabel = if (row.isInClass) "+1" else "-1"

      /** 1-indexing features here, to match heart_scale */
      classLabel + " " + (row.features.zipWithIndex map { case (feature, index) =>
        s"${index + 1}:$feature"
      }).mkString(" ")
    }
}
