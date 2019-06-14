package com.yangee.cores

import java.io.File

import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.recommendation.ALS
import org.apache.spark.sql.{DataFrame, SparkSession}

object ALSTest2ML {

  case class Rating(userId: Int, movieId: Int, rating: Float, timestamp: Long)

  def parseRating(str: String): Rating = {
    val fields = str.split(",")
    assert(fields.size == 4)
    Rating(fields(0).toInt, fields(1).toInt, fields(2).toFloat, fields(3).toLong)
  }


  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local[2]")
      .appName("MovieLensALS").getOrCreate()

    val movieLensHomeDir = "/Users/hiwes/Downloads/ml-latest-small/"
    // val ratings = spark.read.format("csv")
    //   .option("header", "true")
    //   .load(new File(movieLensHomeDir, "ratings.csv").toString)
    //   .map(parseRating)
    //   .toDF("userId", "movieId", "rating", "timestamp")
    import spark.implicits._
    val ratings = spark.read.textFile(new File(movieLensHomeDir, "ratings.dat").toString).map(parseRating).toDF("userId", "movieId", "rating", "timestamp")

    val Array(training, test) = ratings.randomSplit(Array(0.8, 0.2))

    val als = new ALS()
      .setMaxIter(5)
      .setRegParam(0.1)
      .setUserCol("userId")
      .setItemCol("movieId")
      .setRatingCol("rating")
    val model = als.fit(training)

    // 通过计算测试数据上的RMSE来评估模型
    // 注意，将冷启动策略设置为“drop”，以确保不获得NaN评估指标
    model.setColdStartStrategy("drop")
    val predictions = model.transform(test)

    val evaluator = new RegressionEvaluator() // 创建回归评估器
      .setMetricName("rmse")
      .setLabelCol("rating")
      .setPredictionCol("prediction")
    val rmse = evaluator.evaluate(predictions)

    val evaluator2 = new RegressionEvaluator() // 创建评估器
      .setMetricName("mae")
      .setLabelCol("rating")
      .setPredictionCol("prediction")
    val mae = evaluator2.evaluate(predictions)
    println(s"均方根误差RMSE = $rmse")

    // 为每个用户生成十大电影推荐
    val userRecs: DataFrame = model.recommendForAllUsers(10)
    // 为每部电影生成十大用户推荐
    val movieRecs: DataFrame = model.recommendForAllItems(10)

    userRecs.show(false)
    movieRecs.show(false)

    println("开始打印针对用户的推荐结果:")
    userRecs.foreachPartition(ite => {
      ite.foreach(row => {
        println(row.getAs("recommendations"))
      })
    })


    // SD标准差Standard Deviatio:  是方差的算数平方根。是用来衡量一组数自身的离散程度.
    // RMSE:  观测值与真实值偏差的平方和与观测次数m比值的平方根,用来衡量观测值同真值之间的偏差.
    // MAE:   是绝对误差的平均值,能更好地反映预测值误差的实际情况.
    // =======RMSE与标准差SD对比=======:
    // SD标准差是用来衡量一组数自身的离散程度,
    // RMSE均方根误差是用来衡量观测值同真值之间的偏差.
    // 它们的研究对象和研究目的不同,但是计算过程类似.
    // =======RMSE与MAE对比=======:
    // RMSE相当于L2范数,MAE相当于L1范数.次数越高,计算结果就越与较大的值有关,而忽略较小的值.
    // 所以这就是为什么RMSE针对异常值更敏感的原因（即有一个预测值与真实值相差很大,那么RMSE就会很大）.
    println(s"均方根误差RMSE = $rmse")
    println(s"平均绝对误差MAE = $mae")
    val numRatings: Long = ratings.count()
    val numUser = ratings.map(row => row.getAs("userId").toString).distinct().count()
    val numMovie = ratings.map(row => row.getAs("movieId").toString).distinct().count()
    println("获取 " + numRatings + " 条评分,从 " + numUser + " 个用户对 " + numMovie + " 部电影.")
    spark.stop()
  }
}

/**
  * 5-0.01
  * 均方根误差RMSE = 1.0832589677260678
  * 平均绝对误差MAE = 0.8124989234848584
  * 10-0.1
  * 均方根误差RMSE = 0.8949249617118905
  * 平均绝对误差MAE = 0.6856687844922466
  * 5-0.1
  * 均方根误差RMSE = 0.8902980711745111
  * 平均绝对误差MAE = 0.6835127417357405
  * 6-0.08
  * 均方根误差RMSE = 0.8962373768239761
  * 平均绝对误差MAE = 0.6867443081409842
  */