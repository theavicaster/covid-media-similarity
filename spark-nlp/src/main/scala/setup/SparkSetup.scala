package text.similarity
package setup

import org.apache.spark.sql.SparkSession

import java.io.{FileNotFoundException, InputStream}
import java.util.Properties


abstract class SparkSetup(appName: String) {

  val resourceStream: InputStream = getClass.getResourceAsStream("/config.properties")
  val CONFIG: Properties = new Properties()

  if (resourceStream != null) {
    CONFIG.load(resourceStream)
  } else {
    throw new FileNotFoundException("Configuration file could not be loaded")
  }

  val RECENT_WINDOW_MINUTES: Int = CONFIG.getProperty("RECENT_WINDOW_MINUTES").toInt
  val MONGODB_INPUT_URI = sys.env("MONGODB_URI")
  val MONGODB_OUTPUT_URI = sys.env("MONGODB_URI")
  val MONGODB_INPUT_COLLECTION = CONFIG.getProperty("spark.mongodb.input.collection")
  val SECOND_COLLECTION = CONFIG.getProperty("SECOND_COLLECTION")
  val OUTPUT_COLLECTION = CONFIG.getProperty("OUTPUT_COLLECTION")

  val spark: SparkSession = SparkSession
    .builder()
    .appName(appName)
    .config("spark.mongodb.input.uri", MONGODB_INPUT_URI)
    .config("spark.mongodb.output.uri", MONGODB_OUTPUT_URI)
    .config("spark.mongodb.input.collection", MONGODB_INPUT_COLLECTION)
    .getOrCreate()
  spark.sparkContext.setLogLevel("WARN")

}