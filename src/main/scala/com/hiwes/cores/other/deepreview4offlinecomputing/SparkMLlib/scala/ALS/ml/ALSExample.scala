package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala.ALS.ml

import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.recommendation.ALS

import org.apache.spark.sql.SparkSession

/**
  * ALS示例之:ALSExample.scala
  */
object ALSExample {

  case class Rating(userId: Int, movieId: Int, rating: Float, timestamp: Long)

  def parseRating(str: String): Rating = {
    val fields = str.split("::")
    assert(fields.size == 4)
    Rating(fields(0).toInt, fields(1).toInt, fields(2).toFloat, fields(3).toLong)
  }

  def main(args: Array[String]) {
    val spark = SparkSession
      .builder
      .appName("ALSExample")
      .master("local[2]")
      .getOrCreate()
    import spark.implicits._

    val ratings = spark.read.textFile("/Users/hiwes/app/spark-2.2.0-bin-2.6.0/data/mllib/als/sample_movielens_ratings.txt")
      .map(parseRating)
      .toDF()
    val Array(training, test) = ratings.randomSplit(Array(0.8, 0.2))

    // 利用训练数据建立推荐模型
    val als = new ALS()
      .setMaxIter(5)
      .setRegParam(0.01)
      .setUserCol("userId")
      .setItemCol("movieId")
      .setRatingCol("rating")
    val model = als.fit(training)

    // 通过计算测试数据的RMSE来评估模型
    // 注意，我们将cold start策略设置为“drop”，以确保没有得到NaN评估指标
    model.setColdStartStrategy("drop")
    val predictions = model.transform(test)

    val evaluator = new RegressionEvaluator()
      .setMetricName("rmse")
      .setLabelCol("rating")
      .setPredictionCol("prediction")
    val rmse = evaluator.evaluate(predictions)
    println(s"Root-mean-square error = $rmse")

    // 为每个用户生成十大电影推荐
    val userRecs = model.recommendForAllUsers(10)
    // 为每部电影生成10个用户推荐
    val movieRecs = model.recommendForAllItems(10)

    userRecs.show()
    movieRecs.show()

    spark.stop()
  }
}
