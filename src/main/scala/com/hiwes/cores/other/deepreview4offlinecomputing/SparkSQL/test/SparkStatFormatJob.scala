package com.hiwes.cores.other.deepreview4offlinecomputing.SparkSQL.test

import org.apache.spark.sql.SparkSession
;

/**
  * 第一步清洗：抽取出需要的指定列的数据
  */
object SparkStatFormatJob {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().master("local[4]").appName(s"${this.getClass.getSimpleName}").getOrCreate()

    val access = spark.sparkContext.textFile("file:///Users/hiwes/data/logs/output.log")

    //    access.foreach(println)
    access.map(line => {
      val splits = line.split(" ")
      val ip = splits(0)
      /**
        * 原始日志的第四个和第五个字段拼接起来，就是完整的访问时间
        * [10/Nov/2016:00:01:02 +0800]  ===》 yyyy-MM-dd HH:mm:ss
        */
      val time = splits(3) + " " + splits(4)
      val url = splits(11).replaceAll("\"", "")
      val traffic = splits(9)
      DataUtils.parse(time) + "\t" + url + "\t" + traffic + "\t" + ip
    }).saveAsTextFile("file:///Users/hiwes/data/output")

    spark.stop()
  }
}
