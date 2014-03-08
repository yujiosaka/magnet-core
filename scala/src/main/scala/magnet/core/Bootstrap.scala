package magnet.core

object Bootstrap {
  def main(args: Array[String]) {
    val category = args.head
    val words = args.tail
    val trend = TrendData(words)
    val chart = ChartCreator
      .getChart(words)(TrendSearcher.parseTime(trend.rawData))
    S3Uploader.upload(chart, category)
  }
}
