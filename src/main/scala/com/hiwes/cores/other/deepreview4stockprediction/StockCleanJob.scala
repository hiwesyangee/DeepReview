package com.hiwes.cores.other.deepreview4stockprediction

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

/**
  * 完成股票数据的数据清洗
  */
object StockCleanJob {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local[4]")
      .config("spark.sql.parquet.compression.codec", "gzip")
      .config("spark.sql.sources.partitionColumnTypeInferenceEnabled", "false")
      .appName(s"${this.getClass.getSimpleName}")
      .getOrCreate()

    val lines = spark.read.format("csv").option("header", "true").option("Encoding", "GB2312").load("file:///Users/hiwes/Downloads/StockData20170505/*.csv")
    import spark.implicits._

    //    lines.show(false)
    /**
      * 获取到每条股票的每条最高最低价数据
      */
    val enen: DataFrame = lines.map(row => (row.getString(0), row.getString(6).toDouble, row.getString(7).toDouble)).toDF()
    enen.take(10).foreach(println)

    //    val enenRDD: RDD[(String, Double, Double)] = enen.rdd
    //
    //    val need: RDD[(String, Double, Double)] = enenRDD.groupBy(_._1).map(tup => {
    //      val id: String = tup._1
    //      var max: Double = 0d
    //      var min: Double = 100d
    //      // 遍历，找到每个股票的历史最低价和历史最高价
    //      tup._2.foreach(tup => {
    //        if (tup._2 < min) {
    //          min = tup._2
    //        }
    //        if (tup._3 > max) {
    //          max = tup._3
    //        }
    //      })
    //      (id, min, max)
    //    })
    //    //    need.take(10).foreach(println)
    //
    //    need.map(tup => (tup._1, tup._3 - tup._2)).sortBy(_._2, false).take(10).foreach(println)

    //    enen.createOrReplaceTempView("data")
    //    val what: DataFrame = spark.sql("select 股票代码,最低价，最高价 from data ")
    //    what.take(10).foreach(println)

    spark.stop()
  }

}
