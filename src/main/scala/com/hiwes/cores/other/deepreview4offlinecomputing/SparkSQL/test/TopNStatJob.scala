//package com.hiwes.cores.other.deepreview4offlinecomputing.SparkSQL.test
//
//import com.hiwes.cores.other.deepreview4offlinecomputing.SparkSQL.newTest.SparkDAO
//import org.apache.spark.sql.{DataFrame, SparkSession}
//
//import scala.collection.mutable.ListBuffer
//;
//
///**
//  * TopN统计Spark作业
//  */
//object TopNStatJob {
//  def main(args: Array[String]): Unit = {
//    val spark = SparkSession
//      .builder()
//      .appName(s"${this.getClass.getSimpleName}")
//      .config("sparl.sql.sources.partitionColumnTypeInference.enabled", false)
//      .master("local[4]")
//      .getOrCreate()
//
//    val accessDF = spark.read.format("parquet").load("file:///Users/hiwes/data/clean")
//
//    accessDF.printSchema()
//    accessDF.show(false)
//
//    // 最受欢迎的TopN课程
//    videoAccessTopNStat(spark, accessDF)
//
//    /**
//      * 实现最受欢迎的TopN课程
//      */
//    def videoAccessTopNStat(spark: SparkSession, accessDF: DataFrame): Unit = {
//      // DataFrame 的API
//      //      import spark.implicits._
//      //      val videoAccessTopNDF = accessDF.filter($"day" === "20170511" && $"cmsType" === "video")
//      //        .groupBy("day", "cmsId")
//      //        .agg(count("cmsId").as("times"))
//      //        .orderBy($"times".desc)
//      //
//      //      videoAccessTopNDF.show(false)
//
//      // sql 的API
//      accessDF.createOrReplaceTempView("access_log")
//      val videoAccessTopNDF = spark.sql("select day,cmsId ,count(1) as times from access_log " +
//        "where day = '20170511' and cmsType = 'video' " +
//        "group by day,cmsId order by times desc")
//
//      //      videoAccessTopNDF.show(false)
//
//      // 将统计结果写入到MySQL中
//      try {
//        videoAccessTopNDF.foreachPartition(ite => {
//          val list = new ListBuffer[DayVideoAccessStat]
//          ite.foreach(row => {
//            val day = row.getAs[String]("day")
//            val cmsId = row.getAs[Long]("cmsId")
//            val times = row.getAs[Long]("times")
//            list.append(DayVideoAccessStat(day, cmsId, times))
//          })
//
//          SparkDAO.insertDayVideoAccessTopN(list)
//        })
//      } catch {
//        case e: Exception => e.printStackTrace()
//      }
//    }
//
//    spark.stop()
//
//  }
//
//}
