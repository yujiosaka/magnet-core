package magnet.core

import org.freaknet.gtrends.api._
import org.apache.http.impl.client.DefaultHttpClient

object TrendSearcher {

  val user = "testmail.magnet@gmail.com"

  val password = "rS5AxI6zYGpP"

  val errorToken = "An error has been detected"

  def search(words: Array[String]): String = {
    GoogleConfigurator.getConfiguration
      .setProperty("google.auth.reIsLoggedIn", ".*")
    val client = new DefaultHttpClient()
    val authenticate = new GoogleAuthenticator(user, password, client)
    val trendClient = new GoogleTrendsClient(authenticate, client)
    val param = words.map(_.trim).mkString(",")
    val trendRequest = new GoogleTrendsRequest(param)
    execute(trendClient)(trendRequest)(0)
  }

  private def execute(client: GoogleTrendsClient)(request: GoogleTrendsRequest)(count: Int): String = {
    val result = client.execute(request)
    if (result.contains(errorToken)) {
      if (count < 10) {
        Thread.sleep(1000)
        execute(client)(request)(count + 1)
      } else {
        throw new IllegalStateException("Failed to search trend data")
      }
    } else {
      result
    }
  }

  def parseTime(rawData: String): List[(String, Array[String])] = {
    val parser = new GoogleTrendsCsvParser(rawData)
    parser.getSectionAsString("Interest over time", true)
      .lines.map(parseLine).toList
  }

  private def parseLine(line: String): (String, Array[String]) = {
    val params = line.split(",")
    params.head.split(" - ")(1) -> parseValues(params.tail)
  }

  private def parseValues(values: Array[String]): Array[String] =
    values.map(value => {
      val trimmed = value.trim
      if (trimmed.isEmpty)
        "-1"
      else
        trimmed
    })
}
