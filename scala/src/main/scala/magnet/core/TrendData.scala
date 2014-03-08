package magnet.core

class TrendData(val words: Array[String], val rawData: String)

object TrendData {
  def apply(words: Array[String]): TrendData = {
    new TrendData(words, TrendSearcher.search(words))
  }
}
