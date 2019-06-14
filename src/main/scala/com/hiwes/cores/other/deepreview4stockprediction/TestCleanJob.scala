package com.hiwes.cores.other.deepreview4stockprediction

import org.apache.spark.sql._

object TestCleanJob {
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
    //    val enen: DataFrame = lines.map(row => Row(row.getString(0), row.getString(6).toDouble, row.getString(7).toDouble)).toDF()
    //    enen.take(10).foreach(println)

    val enen: DataFrame = lines.select("股票代码", "最低价", "最高价")

    enen.sort("股票代码", "最低价", "最高价").take(10).foreach(println)



    //    enen.take(10).foreach(println)


    spark.stop()
  }
}
