package magnet.core

import com.mongodb.casbah.Imports._
import com.mongodb.ServerAddress
import java.util.Date

class MongoDao {

  val url = "tempest.mongohq.com"

  val port = 10045

  val database = "magnet-large"

  val user = "user"

  val password = "testtest"

  val dateFormat = "%1$tY%1$tm"

  private val client = MongoClient(
    new ServerAddress(url, port),
    List(MongoCredential.createMongoCRCredential(
      user, database, password.toCharArray)))

  def close() {
    client.close()
  }

  def findPhrases(genre: String)(date: Date): Array[String] = {
    val dateParam = dateFormat format date
    val db = client.getDB(database)
    val collection = db("key_phrase_summaries")
    collection.find(MongoDBObject("category" -> genre,
                                  "year_month" -> dateParam,
                                  "is_skill" -> true))
              .sort(Map("total_score" -> -1))
              .limit(5)
              .map(_.get("key_phrase").toString)
              .toArray
  }

  def insertResult(genre: String,
                   words: Array[String],
                   url: String,
                   rawData: String)(date: Date) {
    val dateParam = dateFormat format date
    val db = client.getDB(database)
    val collection = db("trend_charts")
    val result = MongoDBObject("category" -> genre,
                               "year_month" -> dateParam,
                               "words" -> words,
                               "image_url" -> url,
                               "raw_data" -> rawData,
                               "created_at" -> new Date())
    collection.insert(result)
  }
}

object MongoDao {
  def apply(): MongoDao = new MongoDao
}
