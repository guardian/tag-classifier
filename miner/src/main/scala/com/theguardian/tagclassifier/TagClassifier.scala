package com.theguardian.tagclassifier

import java.io._
import util.IntegerString

object TagClassifier extends App {
  val DefaultDataSetSize = 20000

  val BadParamsErrorCode = 1
  val BadOutputDirectoryErrorCode = 2
  val FilesAlreadyExistErrorCode = 3

  case class OptionParseError(
    errorMessage: String,
    showUsage: Boolean = false
  )

  object Options {
    def empty = Options(None, None, None)
  }

  case class Options(
    dataSetSize: Option[Int],
    tagId: Option[String],
    outputDirectory: Option[String]
  )

  val UsageString = """Usage: miner [--data-set-size num] [--output-directory dir] --tag-id tag"""

  override def main(args: Array[String]) {
    def consumeOptions(args: List[String], accumulator: Options): Either[OptionParseError, Options] = args match {
      case "--data-set-size" :: IntegerString(size) :: rest =>
        consumeOptions(rest, accumulator.copy(dataSetSize = Some(size)))

      case "--data-set-size" :: badValue =>
        Left(OptionParseError(s"Data set size must be an integer value: $badValue"))

      case "--tag-id" :: tagId :: rest =>
        consumeOptions(rest, accumulator.copy(tagId = Some(tagId)))

      case "--output-directory" :: outputDirectory :: rest =>
        consumeOptions(rest, accumulator.copy(outputDirectory = Some(outputDirectory)))

      case Nil => Right(accumulator)

      case unexpectedArgument :: _ =>
        Left(OptionParseError(s"Unexpected argument: $unexpectedArgument", showUsage = true))
    }

    consumeOptions(args.toList, Options.empty) match {
      case Left(OptionParseError(errorMessage, showUsage)) =>
        System.err.println(errorMessage)
        if (showUsage) {
          System.err.println(UsageString)
        }
        System.exit(BadParamsErrorCode)

      case Right(Options(dataSetSize, Some(tagId), outputDir)) =>
        val directory = outputDir.getOrElse(new File(".").getCanonicalPath)

        def createFileOrDie(file: File) {
          if (file.exists) {
            System.err.println(s"${file.getCanonicalPath} already exists")
            System.exit(FilesAlreadyExistErrorCode)
          }

          if (!file.createNewFile()) {
            System.err.println(s"Could not create ${file.getCanonicalPath}")
          }
        }

        val featuresFile = new File(directory, "features")

        createFileOrDie(featuresFile)

        val dataSetFile = new File(directory, "data")

        createFileOrDie(dataSetFile)

        if (featuresFile.canWrite && dataSetFile.canWrite) {
          println("Training ...")
          val dataSet = TrainingSetBuilder.build(tagId, dataSetSize.getOrElse(DefaultDataSetSize))
          println("Trained! Writing to files ...")

          val featuresWriter = new PrintWriter(featuresFile)
          featuresWriter.write(dataSet.columns.mkString(" ") + "\n")
          featuresWriter.close()

          val dataSetWriter = new PrintWriter(dataSetFile)

          LibSvmFormatter.format(dataSet) foreach { row =>
            dataSetWriter.write(row + "\n")
          }

          dataSetWriter.close()
        } else {
          System.err.println("Do not have permission to write to the output directory")
          System.exit(BadOutputDirectoryErrorCode)
        }

      case Right(Options(_, None, _)) =>
        System.err.println("You must supply a tag ID")
        System.err.println(UsageString)
        System.exit(BadParamsErrorCode)
    }
  }
}
