package com.theguardian.tagclassifier.data

import com.amazonaws.services.s3._
import de.bwaldvogel.liblinear.Model
import scala.pickling._
import scala.pickling.binary._
import java.io.ByteArrayInputStream
import com.amazonaws.services.s3.model.ObjectMetadata
import org.apache.commons.io.IOUtils
import scala.util.Try
import grizzled.slf4j.Logging
import com.theguardian.tagclassifier.models.FeatureRange

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

case class S3Storage(client: AmazonS3Client, bucket: String) extends Logging {
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

    val pickled: BinaryPickle = serialized.pickle(binary.pickleFormat)

    logger.info(s"Uploading ${pickled.value.length.toDouble / 1024}kb to S3")

    val inputStream = new ByteArrayInputStream(pickled.value)

    val metadata = new ObjectMetadata()
    metadata.setContentLength(pickled.value.length.toLong)

    client.putObject(bucket, key(modelInfo.tagId), inputStream, metadata)
  }

  def loadModel(modelId: String) = {
    logger.info(s"Loading model $modelId from $bucket")

    /** TODO error handling here */
    for {
      ModelSerialization(tagId, modelString, features, ranges, testInfo) <- Try {
        IOUtils.toByteArray(client.getObject(bucket, modelId).getObjectContent).unpickle[ModelSerialization]
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
