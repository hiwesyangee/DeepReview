package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala.ALS.ml

import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.recommendation.{ALS, ALSModel}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object OwnALSExample2 {

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

    var bestIter: Int = 0
    var bestParam: Double = 0.0
    var bestRMSE: Double = 10.0
    val arr: Array[Double] = Array(0.01, 0.03, 0.05, 0.07, 0.1, 0.3, 0.5)

    for (i: Int <- 5 to 15) {
      for (p: Double <- arr) {
        val als = new ALS()
          .setMaxIter(i)
          .setRegParam(p)
          .setUserCol("userId")
          .setItemCol("movieId")
          .setRatingCol("rating")
        val model: ALSModel = als.fit(training)
        model.setColdStartStrategy("drop") // 设定冷启动策略
        val predictions: DataFrame = model.transform(test) // 获取预测DataFrame
        val evaluator = new RegressionEvaluator()
          .setMetricName("rmse")
          .setLabelCol("rating")
          .setPredictionCol("prediction")

        val rmse: Double = evaluator.evaluate(predictions)
        if (rmse <= bestRMSE) {
          bestRMSE = rmse
          bestIter = i
          bestParam = p
        }
      }
    }

    val als = new ALS()
      .setMaxIter(bestIter)
      .setRegParam(bestParam)
      .setUserCol("userId")
      .setItemCol("movieId")
      .setRatingCol("rating")
    val model: ALSModel = als.fit(training)
    model.setColdStartStrategy("drop") // 设定冷启动策略
    val predictions: DataFrame = model.transform(test) // 获取预测DataFrame
    val evaluator = new RegressionEvaluator()
      .setMetricName("rmse")
      .setLabelCol("rating")
      .setPredictionCol("prediction")

    val rmse: Double = evaluator.evaluate(predictions)

    val userRecs: DataFrame = model.recommendForAllUsers(10)
    val itemRecs: DataFrame = model.recommendForAllItems(10)

    userRecs.show(false)
    itemRecs.show(false)
    println(s"Root-mean-square error = $rmse" + s" ,bestIter = $bestIter" + s" ,bestParam = $bestParam")
    //    println(s"Root-mean-square error = $rmse")
    //    println(s"bestIter = $bestIter")
    //    println(s"bestParam = $bestParam")
    //    Root-mean-square error = 0.9859821481510723
    //    bestIter = 9
    //    bestParam = 0.1

    spark.stop()
  }
}
