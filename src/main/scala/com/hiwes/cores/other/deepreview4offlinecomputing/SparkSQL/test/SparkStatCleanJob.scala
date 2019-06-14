//package com.hiwes.cores.other.deepreview4offlinecomputing.SparkSQL.test
//
//import com.hiwes.cores.other.deepreview4offlinecomputing.SparkSQLUtils.AccessConvertUtil
//import org.apache.spark.sql.{SaveMode, SparkSession}
//;
//
///**
//  * 使用Spark完成数据清洗操作 ， 第二步
//  */
//object SparkStatCleanJob {
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
//    // RDD=>DataFrame
//    // 传递两个参数，进行转换，（参数1：RDD[Row]，参数2：StructType）
//    val accessDF = spark.createDataFrame(
//      lines.map(x => AccessConvertUtil.parseLog(x)), AccessConvertUtil.struct)
//
//    accessDF.coalesce(1).write.format("parquet").mode(SaveMode.Overwrite).partitionBy("day")
//      .save("/Users/hiwes/data/clean")
//
//    //    lines.foreach(println)
//
//
//    spark.stop()
//  }
//
//}
