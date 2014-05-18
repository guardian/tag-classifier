package data

import com.theguardian.tagclassifier.data.S3Storage
import conf.TrainerConfiguration
import com.amazonaws.services.s3.AmazonS3Client

object TrainerS3Storage {
  private val s3Client = new AmazonS3Client()

  val storage = S3Storage(s3Client, TrainerConfiguration.s3Bucket)
}
