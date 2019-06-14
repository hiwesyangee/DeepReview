package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala.ALS.ml

import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.recommendation.{ALS, ALSModel}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}


/**
  * ALS自写示例之:OwnALSExample.scala
  */
object OwnALSExample {

  case class Rating(userId: Int, movieId: Int, rating: Float, timeStamp: Long)

  def parseRating(line: String): Rating = {
    val arr = line.split("::")
    assert(arr.length == 4)
    Rating(arr(0).toInt, arr(1).toInt, arr(2).toFloat, arr(3).toLong)
  }

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local[4]")
      .appName("OwnALSExample")
      .getOrCreate()
    import spark.implicits._
    val data: Dataset[String] = spark.read.textFile("file:///Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/data/mllib/als/sample_movielens_ratings.txt")

    val df: DataFrame = data.map(parseRating).toDF()

    val Array(training, test) = df.randomSplit(Array(0.8, 0.2))

    val als = new ALS()
      .setMaxIter(4)
      .setRegParam(0.3)
      .setUserCol("userId")
      .setItemCol("movieId")
      .setRatingCol("rating")
    val model: ALSModel = als.fit(training) // 创建ALS模型，类型为ALSModel

    model.setColdStartStrategy("drop") // 设定冷启动策略

    val predictions: DataFrame = model.transform(test) // 获取预测DataFrame

    val evaluator = new RegressionEvaluator()
      .setMetricName("rmse")
      .setLabelCol("rating")
      .setPredictionCol("prediction")

    val rmse: Double = evaluator.evaluate(predictions)

    val userRecs = model.recommendForAllUsers(10)
    val itemRecs = model.recommendForAllItems(10)

    userRecs.show(false)
    itemRecs.show(false)
    println(s"Root-mean-square error = $rmse")

    spark.stop()

  }

}
