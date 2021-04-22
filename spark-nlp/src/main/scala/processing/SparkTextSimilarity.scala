package text.similarity
package processing

import schema.{CommentSchema, TweetSchema}
import setup.SparkSetup
import utilities.CosineSimilarity.cosineSimilarity

import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.ReadConfig
import org.apache.spark.ml.feature.{StopWordsRemover, Tokenizer, Word2Vec}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, current_timestamp, length, unix_timestamp}

object SparkTextSimilarity {

  def main(args: Array[String]): Unit = {
    SparkTextSimilarityProcessor("Social Media Similarity")
  }
}

class SparkTextSimilarityProcessor(appName: String)
  extends SparkSetup(appName: String) {

  val tweets = MongoSpark.load[TweetSchema](spark)

  // overriding to get multiple collections!
  val readConfig = ReadConfig(Map("collection" -> SECOND_COLLECTION), Some(ReadConfig(spark)))
  val comments = MongoSpark.load[CommentSchema](spark, readConfig)


  // keep only rows that were downloaded within the recent window
  // because of Spark's lazy execution we aren't filtering in Spark,
  // MongoDB's filters are used in the query plan
  def filterRecent(df: DataFrame): DataFrame = {
    df.withColumn("downloadedAt", unix_timestamp(col("downloadedAt"), "yyyy-MM-dd HH:mm:ss"))
      .filter((unix_timestamp(current_timestamp()) - col("downloadedAt")) / 60 < RECENT_WINDOW_MINUTES)
      .filter(length(col("body")) < 150) // short text!
  }

  val recentTweets = filterRecent(tweets)
  val recentComments = filterRecent(comments)


  val tokenizer = new Tokenizer()
    .setInputCol("body")
    .setOutputCol("bodyTokens")
  val tokenizedTweets = tokenizer.transform(recentTweets)
  val tokenizedComments = tokenizer.transform(recentComments)


  val stopWordsRemover = new StopWordsRemover()
    .setInputCol("bodyTokens")
    .setOutputCol("bodyFiltered")
  val filteredTweets = stopWordsRemover.transform(tokenizedTweets)
  val filteredComments = stopWordsRemover.transform(tokenizedComments)


  val trainingDf = filteredTweets.select("bodyFiltered")
    .unionByName(filteredComments.select("bodyFiltered"))
  val word2vec = new Word2Vec()
    .setInputCol("bodyFiltered")
    .setOutputCol("wordVector")
    .setMaxIter(25)
    .setMinCount(1)
  val w2vModel = word2vec.fit(trainingDf)


  // average of word2vec vectors
  val doc2VecTweets = w2vModel.transform(filteredTweets)
  val doc2VecComments = w2vModel.transform(filteredComments)
  val doc2VecSimilarity = spark.sql("SELECT tweets.body AS tweetBody, tweets.createdAt as tweetCreatedAt, " +
    "comments.body AS commentBody, comments.createdAt as commentCreatedAt, " +
    "comments.linkTitle as commentLinkTitle, comments.permalink as commentPermalink, " +
    "cosineSimilarity(tweets.wordVector, comments.wordVector) AS similarity " +
    "FROM tweets CROSS JOIN comments " +
    "ORDER BY similarity DESC " +
    "LIMIT 200")

  spark.udf.register("cosineSimilarity", cosineSimilarity)

  doc2VecTweets.createOrReplaceTempView("tweets")
  doc2VecComments.createOrReplaceTempView("comments")

  doc2VecSimilarity.select("similarity", "tweetBody", "commentBody")
    .show(50, truncate = false)

  MongoSpark.save(doc2VecSimilarity.write.option("collection", OUTPUT_COLLECTION).mode("overwrite"))

}

object SparkTextSimilarityProcessor {
  def apply(appName: String): SparkTextSimilarityProcessor =
    new SparkTextSimilarityProcessor(appName)
}
