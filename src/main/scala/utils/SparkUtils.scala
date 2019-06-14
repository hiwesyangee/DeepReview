package utils

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

object SparkUtils {
  private val conf = new SparkConf().setAppName("SparkMLlib").setMaster("local[4]")
  private val sc = new SparkContext(conf)
  private val spark = SparkSession.builder().config(conf).getOrCreate()

  def getSparkContext(): SparkContext = {
    sc
  }

  def getSparkSession(): SparkSession = {
    spark
  }
}
