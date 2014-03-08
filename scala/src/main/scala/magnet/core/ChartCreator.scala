package magnet.core

import java.text.SimpleDateFormat
import java.util.Calendar
import scala.io.Source

object ChartCreator {

  val urlPattern = "http://chart.apis.google.com/chart?chs=%s&chd=%s&cht=%s&chdl=%s&chco=%s&chxt=%s&chxl=%s"

  val datePattern = "yyyy-MM-dd"

  val chartSize = "500x500"

  val chartType = "lc"

  val labelType = "x,y"

  val colors = "ff0000,0000ff,00ff00,ffff00,00ffff"

  def getChart(words: Array[String])(data: List[(String, Array[String])]):
  Array[Byte] = {
    val partData = partialData(data)
    val rawData = (0 until words.size).map(
      index => partData.map(_._2(index)))
    val chartData = "t:" + rawData.map(_.mkString(",")).mkString("|")
    val labelValue = "0:|" + partData.head._1 + "|" + partData.last._1 + "|1:|0|100"
    val notes = words.mkString("|")
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
