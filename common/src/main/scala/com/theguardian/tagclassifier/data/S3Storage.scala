package com.theguardian.tagclassifier.data

import com.amazonaws.services.s3._
import de.bwaldvogel.liblinear.Model
import java.io.ByteArrayInputStream
import com.amazonaws.services.s3.model.ObjectMetadata
import org.apache.commons.io.IOUtils
import scala.util.Try
import grizzled.slf4j.Logging
import com.theguardian.tagclassifier.models.FeatureRange
import play.api.libs.json.Json

case class ModelSerialization(
  tagId: String,
  serializedModel: String,
  features: List[String],
  ranges: List[FeatureRange],
  testingInfo: TestingInfo
)

case class TestingInfo(
  attempts: Int,
  successes: Int
) {
  def successRate = successes.toDouble / attempts
}

case class ModelInfo(
  tagId: String,
  features: List[String],
  ranges: List[FeatureRange],
  model: Model,
  testingInfo: TestingInfo
)

object JsonImplicits {
  implicit val featureRangeFormat = Json.format[FeatureRange]
  implicit val testingInfoFormat = Json.format[TestingInfo]
  implicit val modelSerializationFormat = Json.format[ModelSerialization]
}

case class S3Storage(client: AmazonS3Client, bucket: String) extends Logging {
  import JsonImplicits._

  val Encoding = "utf-8"

  def key(tagId: String) = tagId.replace("/", "-")

  def storeModel(modelInfo: ModelInfo) = Try {
    logger.info(s"Storing model for ${modelInfo.tagId} in $bucket")

    val serialized = ModelSerialization(
      modelInfo.tagId,
      modelInfo.model.serializeToString,
      modelInfo.features,
      modelInfo.ranges,
      modelInfo.testingInfo
    )

    val pickled = Json.stringify(Json.toJson(serialized))
    val jsonBytes = pickled.getBytes(Encoding)

    logger.info(s"Uploading ${jsonBytes.length.toDouble / 1024}kb to S3")

    val inputStream = new ByteArrayInputStream(jsonBytes)
    val metadata = new ObjectMetadata()
    metadata.setContentLength(jsonBytes.length)

    client.putObject(bucket, key(modelInfo.tagId), inputStream, metadata)
  }

  def loadModel(modelId: String) = {
    logger.info(s"Loading model $modelId from $bucket")

    /** TODO error handling here */
    for {
      ModelSerialization(tagId, modelString, features, ranges, testInfo) <- Try {
        Json.fromJson[ModelSerialization](Json.parse(
          new String(IOUtils.toByteArray(client.getObject(bucket, modelId).getObjectContent), Encoding)
        )).get
      }
      model <- modelFromString(modelString)
    } yield ModelInfo(tagId, features, ranges, model, testInfo)
  }

  def accessControlList = {
    logger.info(s"Getting ACL for $bucket")

    Try {
      client.getBucketAcl(bucket)
    }
  }
}
