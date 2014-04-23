package com.theguardian.tagclassifier

import util.IntegerString

object TagClassifier extends App {
  val DefaultDataSetSize = 20000
  val BadParamsErrorCode = 1

  case class OptionParseError(
    errorMessage: String,
    showUsage: Boolean = false
  )

  object Options {
    def empty = Options(None, None)
  }

  case class Options(
    dataSetSize: Option[Int],
    tagId: Option[String]
  )

  val UsageString = """Usage: miner [--data-set-size num] --tag-id tag"""

  override def main(args: Array[String]) {
    def consumeOptions(args: List[String], accumulator: Options): Either[OptionParseError, Options] = args match {
      case "--data-set-size" :: IntegerString(size) :: rest =>
        consumeOptions(rest, accumulator.copy(dataSetSize = Some(size)))

      case "--data-set-size" :: badValue =>
        Left(OptionParseError(s"Data set size must be an integer value: $badValue"))

      case "--tag-id" :: tagId :: rest =>
        consumeOptions(rest, accumulator.copy(tagId = Some(tagId)))

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

      case Right(Options(dataSetSize, Some(tagId))) =>
        TrainingSetBuilder.build(tagId, dataSetSize.getOrElse(DefaultDataSetSize))

      case Right(Options(_, None)) =>
        System.err.println("You must supply a tag ID")
        System.err.println(UsageString)
        System.exit(BadParamsErrorCode)
    }
  }
}
