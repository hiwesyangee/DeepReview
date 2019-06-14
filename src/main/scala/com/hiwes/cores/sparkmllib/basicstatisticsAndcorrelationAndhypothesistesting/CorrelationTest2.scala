package com.hiwes.cores.sparkmllib.basicstatisticsAndcorrelationAndhypothesistesting

import org.apache.spark.ml.stat.Correlation
import org.apache.spark.mllib.stat._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import utils.SparkUtils

object CorrelationTest2 {
  def main(args: Array[String]): Unit = {
    val sc = SparkUtils.getSparkContext()

    val line = sc.textFile("beijing.txt").flatMap(_.split(",")).map(_.toDouble)
    val years: RDD[Double] = line.filter(_ > 1000)
    val values: RDD[Double] = line.filter(_ < 1000)

    val result: Double = Statistics.corr(years, values, "spearman")
    println(result)

    val spark = SparkSession.builder().config(sc.getConf).getOrCreate()
    import spark.implicits._



  }
}
