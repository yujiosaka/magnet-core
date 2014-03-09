package magnet.core

import java.util.Calendar
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat

object Bootstrap {

  val logger = LoggerFactory.getLogger(classOf[Bootstrap])

  val dateFormat = "yyyyMM"

  val categoryMap = Map("開発" -> "development",
                        "デザイン" -> "design",
                        "ライティング" -> "writing",
                        "事務" -> "office")

  def main(args: Array[String]) {
    val cal = Calendar.getInstance()
    if (! args.isEmpty) {
      cal.setTime(new SimpleDateFormat(dateFormat).parse(args(0)))
      cal.add(Calendar.MONTH, 1)
    }
    cal.set(Calendar.DAY_OF_MONTH, 1)
    cal.add(Calendar.DAY_OF_MONTH, -1)
    val executeDate = cal.getTime
    cal.add(Calendar.MONTH, -10)
    val trendStartDate = cal.getTime
    val dao = MongoDao()
    try {
      categoryMap.foreach(category => {
        logger.info("Create chartData of [%1$s %2$tY%2$tm]"
          .format(category._1, executeDate))
        val words = dao.findPhrases(category._1)(executeDate)
        logger.info("Top 5 words are %s".format(words.mkString(",")))
        val trend = TrendData(words)
        val (labels, data) = TrendSearcher
          .parseTime(words)(trend.rawData)(trendStartDate, executeDate)
        val chart = ChartCreator.getChart(labels)(data)
        logger.info("Upload chartData of [%1$s %2$tY%2$tm] to Amazon S3"
          .format(category._1, executeDate))
        val url = S3Uploader.upload(chart, category._2)(executeDate)
        logger.info("Insert chartData of [%1$s %2$tY%2$tm] to MongoDB"
          .format(category._1, executeDate))
        dao.insertResult(category._1, words, url, trend.rawData)(executeDate)
        Thread.sleep(30000)
      })
    } finally {
      dao.close()
    }
  }
}

class Bootstrap
