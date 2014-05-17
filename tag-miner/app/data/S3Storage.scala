package data

import com.amazonaws.services.s3._
import de.bwaldvogel.liblinear.Model
import miner.FeatureRange
import scala.pickling._
import scala.pickling.binary._
import conf.TagClassifierConfiguration
import java.io.ByteArrayInputStream
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.regions.{Region, Regions}
import org.apache.commons.io.IOUtils
import scala.util.Try

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

object S3Storage {
  val bucket = TagClassifierConfiguration.s3Bucket

  val client = new AmazonS3Client()
  client.setRegion(Region.getRegion(Regions.EU_WEST_1))

  def key(tagId: String) = tagId.replace("/", "-")

  def storeModel(modelInfo: ModelInfo) {
    val serialized = ModelSerialization(
      modelInfo.tagId,
      modelInfo.model.serializeToString,
      modelInfo.features,
      modelInfo.ranges,
      modelInfo.testingInfo
    )

    val pickled: BinaryPickle = serialized.pickle(binary.pickleFormat)

    val inputStream = new ByteArrayInputStream(pickled.value)

    client.putObject(bucket, key(modelInfo.tagId), inputStream, new ObjectMetadata())
  }

  def loadModel(modelId: String) = {
    /** TODO error handling here */
    for {
      ModelSerialization(tagId, modelString, features, ranges, testInfo) <- Try {
        IOUtils.toByteArray(client.getObject(bucket, modelId).getObjectContent).unpickle[ModelSerialization]
      }
      model <- modelFromString(modelString)
    } yield ModelInfo(tagId, features, ranges, model, testInfo)
  }
}
