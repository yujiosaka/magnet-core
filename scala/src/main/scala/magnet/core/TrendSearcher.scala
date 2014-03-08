package magnet.core

import org.freaknet.gtrends.api._
import org.apache.http.impl.client.DefaultHttpClient

object TrendSearcher {

  val user = "testmail.magnet@gmail.com"

  val password = "rS5AxI6zYGpP"

  val errorToken = "An error has been detected"

  def search(words: Array[String]): List[(String, Array[String])] = {
    GoogleConfigurator.getConfiguration
      .setProperty("google.auth.reIsLoggedIn", ".*")
    val client = new DefaultHttpClient()
    val authenticate = new GoogleAuthenticator(user, password, client)
    val trendClient = new GoogleTrendsClient(authenticate, client)
    val param = words.map(_.trim).mkString(",")
    println(param)
    val trendRequest = new GoogleTrendsRequest(param)
    val result = trendClient.execute(trendRequest)
    if (result.contains(errorToken))
      throw new IllegalStateException("Failed to search trend data")
    val parser = new GoogleTrendsCsvParser(result)
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
