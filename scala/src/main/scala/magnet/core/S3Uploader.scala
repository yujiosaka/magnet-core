package magnet.core

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.model.{CannedAccessControlList, ObjectMetadata}
import java.io.ByteArrayInputStream
import java.util.Date

object S3Uploader {

  val filePattern = "chartData_%1$tY%1$tm_%2$s.png"

  val endPoint = "https://s3-ap-northeast-1.amazonaws.com"

  val accessKey = "AKIAJFNLFPXZJIOO5TQA"

  val secretKey = "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"

  val bucket = "magnet-box"

  def upload(image: Array[Byte], category: String)(date: Date):String = {
    val fileName = filePattern.format(date, category)
    val client = new AmazonS3Client(
      new BasicAWSCredentials(accessKey, secretKey))
    client.setEndpoint(endPoint)
    val metaData = new ObjectMetadata
    metaData.setContentLength(image.length)
    val inputStream = new ByteArrayInputStream(image)
    try {
      client.putObject(bucket, fileName, inputStream, metaData)
      client.setObjectAcl(bucket, fileName,
        CannedAccessControlList.PublicRead)
    }
    inputStream.close()
    endPoint + "/" + bucket + "/" + fileName
  }
}
