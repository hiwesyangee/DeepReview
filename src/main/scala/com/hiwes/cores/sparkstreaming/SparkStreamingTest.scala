package com.hiwes.cores.sparkstreaming

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Durations, StreamingContext}

object SparkStreamingTest {
  def main(args: Array[String]): Unit = {
    //    test1()
    test2()
  }

  def test1(): Unit = {
    val spark = SparkSession.builder()
      .master("local[2]")
      .appName("master")
      .config("spark.default.parallelism", 500)
      .getOrCreate()

    import spark.implicits._

    val lines = spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", "9999")
      .load()

    val words = lines.as[String].flatMap(_.split(" "))

    val wordCounts: DataFrame = words.groupBy("value").count()

    val query = wordCounts.writeStream
      .outputMode("complete")
      //      .outputMode("update")
      .format("console")
      .start()

    query.awaitTermination()

  }

  val checkpointDirectory = "hdfs://master:8020/opt/checkpoint/"

  def test2(): Unit = {
    val spark = SparkSession.builder().appName("master").master("local[2]").config("spark.default.parallelism", 500).getOrCreate()
    import spark.implicits._

    val ssc = new StreamingContext(spark.sparkContext, Durations.seconds(3))

    ssc.remember(Durations.minutes(5))

    val dstream: DStream[String] = ssc.textFileStream("/Users/hiwes/data/stream/")

    val ned: DStream[Long] = dstream.count()

//    StreamingContext.getOrCreate(checkpointDirectory, )

    ned.foreachRDD(rdd => {
      rdd.foreach(lon => {
        println(lon)
      })
    })

    try {
      ssc.start()
      ssc.awaitTermination()
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
}
