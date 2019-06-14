//package com.hiwes.cores.other.deepreview4offlinecomputing.SparkSQL.newTest
//
//import org.apache.spark.rdd.RDD
//import org.apache.spark.sql.{Row, SaveMode, SparkSession}
//import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}
//
//object SparkClean {
//
//  val struct = StructType(
//    Array(
//      StructField("url", StringType),
//      StructField("cmsType", StringType),
//      StructField("cmsId", LongType),
//      StructField("traffic", LongType),
//      StructField("ip", StringType),
//      StructField("city", StringType),
//      StructField("time", StringType),
//      StructField("day", StringType)
//    )
//  )
//
//  def main(args: Array[String]): Unit = {
//    val spark = SparkSession
//      .builder()
//      .appName(s"${this.getClass.getSimpleName}")
//      .config("spark.sql.parquet.compression.codec", "gzip")
//      .master("local[4]")
//      .getOrCreate()
//
//    val lines = spark.sparkContext.textFile("file:///Users/hiwes/data/logs/access.log")
//
//    //    lines.foreach(println)
//
//    val row: RDD[Row] = lines.map(line => {
//      try {
//        val splits: Array[String] = line.split("\t")
//        val time: String = splits(0)
//        val day: String = time.substring(0, 10).replaceAll("-", "") // 当天
//        //
//        val url: String = splits(1) // 访问url——String
//
//        val domain = "http://www.imooc.com/"
//        val cms = url.substring(url.indexOf(domain) + domain.length)
//        val cmsTypeId = cms.split("/")
//
//        var cmsType: String = ""
//        var cmsId: Long = 0l
//        if (cmsTypeId.length > 1) {
//          cmsType = cmsTypeId(0) // 课程类型
//          cmsId = cmsTypeId(1).toLong // 课程ID
//        }
//
//        val traffic: Long = splits(2).toLong // 消耗流量——Long
//        val ip: String = splits(3) // ip——String
//        val city: String = IpUtils.getCity(ip)
//
//        // String,String,Long,Long,String,String,String,String
//        //        url, cmsType, cmsId, traffic, ip, city, times, day
//        Row(url, cmsType, cmsId, traffic, ip, city, time, day)
//      } catch {
//        case e => Row(0)
//      }
//    })
//
//    val rowDF = spark.createDataFrame(row, struct)
//
//    rowDF.coalesce(1).write.format("parquet").mode(SaveMode.Overwrite)
//      .partitionBy("day").save("/Users/hiwes/data/clean")
//
//  }
//}
