import org.apache.spark.sql.SparkSession

object TemplateApplication {
  def main(args: Array[String]) {
    val spark = SparkSession.builder.appName("Spark Scala Application template").getOrCreate()

    // ...

    spark.stop()
  }
}
