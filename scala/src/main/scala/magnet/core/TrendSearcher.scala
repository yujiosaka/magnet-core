package magnet.core

import org.freaknet.gtrends.api._
import org.apache.http.impl.client.DefaultHttpClient
import scala.collection.mutable.ArrayBuffer
import java.util.Date
import java.text.SimpleDateFormat

object TrendSearcher {

  val user = "testmail.magnet@gmail.com"

  val password = "rS5AxI6zYGpP"

  val errorToken = "An error has been detected"

  val datePattern = "yyyy-MM-dd"

  def search(words: Array[String]): String = {
    GoogleConfigurator.getConfiguration
      .setProperty("google.auth.reIsLoggedIn", ".*")
    val client = new DefaultHttpClient()
    val authenticate = new GoogleAuthenticator(user, password, client)
    val trendClient = new GoogleTrendsClient(authenticate, client)
    val param = words.map(_.trim).mkString(",")
    val trendRequest = new GoogleTrendsRequest(param.replaceAll("・", ""))
    execute(trendClient)(trendRequest)(0)
  }

  private def execute(client: GoogleTrendsClient)(request: GoogleTrendsRequest)(count: Int): String = {
    val result = client.execute(request)
    if (result.contains(errorToken)) {
      if (count < 10) {
        Thread.sleep(5000)
        println("Failed to search trend data, retry")
        execute(client)(request)(count + 1)
      } else {
        throw new IllegalStateException(
          "Failed to search trend data ¥n" + result)
      }
    } else {
      result
    }
  }

  def parseTime(words: Array[String])(rawData: String)
               (startDate: Date, endDate: Date):
  (Array[String], Array[(String, Array[String])]) = {
    val dateFormat = new SimpleDateFormat(datePattern)
    val parser = new GoogleTrendsCsvParser(rawData)
    val timeData = parser.getSectionAsString("Interest over time", false).lines.toArray
    val headers = timeData.head.split(",").zipWithIndex.toMap
    val labels = ArrayBuffer[String]()
    val dataMap = words.map(_ -> ArrayBuffer[String]())
    timeData.tail.foreach(line => {
      val params = line.split(",")
      val label = params(headers("Week")).split(" - ")(1)
      val date = dateFormat.parse(label)
      if (date.getTime > startDate.getTime
          && date.getTime <= endDate.getTime) {
        labels += label
        dataMap.foreach(entry => {
          headers.tail.find(_._1.equalsIgnoreCase(entry._1)) match {
            case Some((header, index)) =>
              entry._2 += getParam(params)(index)
            case _ =>
              entry._2 += "0"
          }
        })
      }
    })
    (labels.toArray,
      dataMap.map(entry => entry._1 -> entry._2.toArray))
  }

  private def getParam(params: Array[String])(index: Int): String = {
    if (params.size <= index) {
      "0"
    } else {
      val param = params(index).trim
      if (param.isEmpty)
        "0"
      else
        param
    }
  }
}
