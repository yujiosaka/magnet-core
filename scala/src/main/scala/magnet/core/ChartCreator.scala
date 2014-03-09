package magnet.core

import java.text.SimpleDateFormat
import java.util.Calendar
import scala.io.Source
import java.net.URLEncoder

object ChartCreator {

  val urlPattern = "http://chart.apis.google.com/chart?chs=%s&chd=%s&cht=%s&chdl=%s&chco=%s&chxt=%s&chxl=%s"

  val datePattern = "yyyy-MM-dd"

  val chartSize = "500x500"

  val chartType = "lc"

  val labelType = "x,y"

  val colors = "ff0000,0000ff,00ff00,ffff00,00ffff"

  def getChart(labels: Array[String])(data: Array[(String, Array[String])]):
  Array[Byte] = {
    val chartData = "t:" + data.map(_._2.mkString(",")).mkString("|")
    val labelValue = "0:|" + labels.head + "|" + labels.last + "|1:|0|100"
    val notes = URLEncoder.encode(data.map(_._1).mkString("|"), "utf-8")
    val url = urlPattern.format(
      chartSize, chartData, chartType, notes, colors, labelType, labelValue)
    val source = Source.fromURL(url)(scala.io.Codec.ISO8859)
    try {
      source.map(_.toByte).toArray
    } finally {
      source.close()
    }
  }

  private def partialData(data: List[(String, Array[String])]):
  List[(String, Array[String])] = {
    val dateFormat = new SimpleDateFormat(datePattern)
    val cal = Calendar.getInstance()
    val end = cal.getTime
    cal.add(Calendar.YEAR, -1)
    val start = cal.getTime
    data.filter(data => {
      val date = dateFormat.parse(data._1)
      date.getTime > start.getTime && date.getTime < end.getTime
    })
  }
}
