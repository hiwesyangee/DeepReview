//package com.hiwes.cores.other.deepreview4offlinecomputing.SparkSQL.newTest
//
//import com.hiwes.cores.other.deepreview4offlinecomputing.SparkSQL.test.{DayCityVideoAccessStat, DayVideoAccessStat, DayVideoTrafficsStat}
//import org.apache.spark.sql.expressions.Window
//import org.apache.spark.sql.{DataFrame, SparkSession}
//import org.apache.spark.sql.functions._
//
//import scala.collection.mutable.ListBuffer
//
//object SparkTopNStat {
//  def main(args: Array[String]): Unit = {
//    val spark = SparkSession
//      .builder()
//      .appName(s"${this.getClass.getSimpleName}")
//      .config("spark.sql.sources.partitionColumnTypeInference.enabled", "false")
//      .master("local[4]")
//      .getOrCreate()
//
//    val accessDF = spark.read.format("parquet").load("file:///Users/hiwes/data/clean")
//
//    accessDF.printSchema()
//    accessDF.show(false)
//
//    val day = "20170511"
//    // 最受欢迎的TopN课程
//    // videoAccessTopNStat(spark, accessDF, day)
//
//    // 地市最受欢迎的TopN课程
//    // cityAccessTopNStat(spark, accessDF, day)
//    // 按流量最受欢迎的TopN课程
//    videoTrafficsTopNStat(spark, accessDF, day)
//
//    spark.stop()
//
//  }
//
//  /**
//    * 实现最受欢迎的TopN课程
//    */
//  def videoAccessTopNStat(spark: SparkSession, accessDF: DataFrame, day: String): Unit = {
//    // DataFrame 的API
//    //      import spark.implicits._
//    //      val videoAccessTopNDF = accessDF.filter($"day" === day && $"cmsType" === "video")
//    //        .groupBy("day", "cmsId")
//    //        .agg(count("cmsId").as("times"))
//    //        .orderBy($"times".desc)
//    //
//    //      videoAccessTopNDF.show(false)
//
//    // sql 的API
//    // 操作:统计课程TopN
//    accessDF.createOrReplaceTempView("access_log")
//    val videoAccessTopNDF = spark.sql("select day, cmsId ,count(1) as times from access_log " +
//      "where day = '20170511' and cmsType = 'video' " +
//      "group by day,cmsId order by times desc")
//
//    // 操作:统计文章TopN
//    //    val videoAccessTopNDF = spark.sql("select day, cmsId ,count(1) as times from access_log " +
//    //      "where day = '20170511' and cmsType = 'article' " +
//    //      "group by day,cmsId order by times desc")
//
//    videoAccessTopNDF.show(false)
//
//    // 将统计结果写入到MySQL中
//    try {
//      videoAccessTopNDF.foreachPartition(ite => {
//        val list = new ListBuffer[DayVideoAccessStat]
//        ite.foreach(row => {
//          val day = row.getAs[String]("day")
//          val cmsId = row.getAs[Long]("cmsId")
//          val times = row.getAs[Long]("times")
//          list.append(DayVideoAccessStat(day, cmsId, times))
//        })
//
//        SparkDAO.insertDayVideoAccessTopN(list)
//        //        SparkDAO.insertDayArticleAccessTopN(list)
//      })
//    } catch {
//      case e: Exception => e.printStackTrace()
//    }
//  }
//
//  /**
//    * 实现地市最受欢迎的TopN课程
//    */
//  def cityAccessTopNStat(spark: SparkSession, accessDF: DataFrame, day: String): Unit = {
//    // DataFrame 的API
//    import spark.implicits._
//
//    val cityAccessTopNDF = accessDF.filter($"day" === day && $"cmsType" === "video")
//      .groupBy("day", "city", "cmsId")
//      .agg(count("cmsId").as("times"))
//
//    cityAccessTopNDF.show(false)
//
//    val top3DF = cityAccessTopNDF.select(
//      cityAccessTopNDF("day"),
//      cityAccessTopNDF("city"),
//      cityAccessTopNDF("cmsId"),
//      cityAccessTopNDF("times"),
//      row_number().over(Window.partitionBy(cityAccessTopNDF("city"))
//        .orderBy(cityAccessTopNDF("times").desc)
//      ).as("times_rank")
//    ).filter("times_rank <=2") //.show(false)  //Top3
//
//    // 将统计结果写入到MySQL中
//    try {
//      top3DF.foreachPartition(partitionOfRecords => {
//        val list = new ListBuffer[DayCityVideoAccessStat]
//
//        partitionOfRecords.foreach(info => {
//          val day = info.getAs[String]("day")
//          val cmsId = info.getAs[Long]("cmsId")
//          val city = info.getAs[String]("city")
//          println(city)
//          val times = info.getAs[Long]("times")
//          val timesRank = info.getAs[Int]("times_rank")
//          list.append(DayCityVideoAccessStat(day, cmsId, city, times, timesRank))
//        })
//
//        SparkDAO.insertDayCityVideoAccessTopN(list)
//      })
//    } catch {
//      case e: Exception => e.printStackTrace()
//    }
//  }
//
//  /**
//    * 实现按流量最受欢迎的TopN课程
//    */
//  def videoTrafficsTopNStat(spark: SparkSession, accessDF: DataFrame, day: String): Unit = {
//    import spark.implicits._
//
//    val cityAccessTopNDF = accessDF.filter($"day" === day && $"cmsType" === "video")
//      .groupBy("day", "cmsId").agg(sum("traffic").as("traffics"))
//      .orderBy($"traffics".desc)
//    //.show(false)
//
//    /**
//      * 将统计结果写入到MySQL中
//      */
//    try {
//      cityAccessTopNDF.foreachPartition(partitionOfRecords => {
//        val list = new ListBuffer[DayVideoTrafficsStat]
//
//        partitionOfRecords.foreach(info => {
//          val day = info.getAs[String]("day")
//          val cmsId = info.getAs[Long]("cmsId")
//          val traffics = info.getAs[Long]("traffics")
//          list.append(DayVideoTrafficsStat(day, cmsId, traffics))
//        })
//
//        SparkDAO.insertDayVideoTrafficsAccessTopN(list)
//      })
//    } catch {
//      case e: Exception => e.printStackTrace()
//    }
//
//  }
//
//}
