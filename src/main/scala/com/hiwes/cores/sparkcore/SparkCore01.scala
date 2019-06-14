package com.hiwes.cores.sparkcore

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Spark Core常用算子.
  * wordCount例子
  */
object SparkCore01 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("WordCount")
    val sc = new SparkContext(conf)

    val wordPath = "/Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/README.md";
    val line = sc.textFile(wordPath)

    val counts: RDD[(String, Int)] = line.flatMap(_.split(" ")).map(word => (word, 1)).reduceByKey { case (x, y) => x + y }

    counts.foreach(println)

    counts.saveAsTextFile("/Users/hiwes/data/enenen.txt")

  }

}
