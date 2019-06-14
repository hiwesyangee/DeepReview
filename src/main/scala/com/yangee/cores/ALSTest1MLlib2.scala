package com.yangee.cores

import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.recommendation.ALS
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, SparkSession}

object ALSTest1MLlib2 {

  case class Rating(userId: Int, movieId: Int, rating: Double)

  case class Movie(movieId: Int, title: String, genres: Seq[String])

  case class User(userId: Int, gender: String, age: Int, occupation: Int, zip: String)

  // 定义解析函数parseMovie
  def parseMovie(str: String): Movie = {
    val fields = str.split("::")
    assert(fields.size == 3)
    Movie(fields(0).toInt, fields(1), Seq(fields(2)))
  }

  // 定义解析函数parseUser
  def parseUser(str: String): User = {
    val fields = str.split("::")
    assert(fields.size == 5)
    User(fields(0).toInt, fields(1), fields(2).toInt, fields(3).toInt, fields(4))
  }

  // 定义解析函数parseRating
  def parseRating(str: String): Rating = {
    val fields = str.split("::")
    assert(fields.size == 4)
    Rating(fields(0).toInt, fields(1).toInt, fields(2).toDouble)
  }

  // 定义主函数
  def main(args: Array[String]): Unit = {
    //    val spark = SparkSession
    //      .builder()
    //      .appName("ALSMovie")
    //      .master("local[4]")
    //      .getOrCreate()
    //    import spark.implicits._
    //    val line: DataFrame = spark.read.textFile("/Users/hiwes/Downloads/ml-latest-small/ratings.dat")
    //      .map(parseRating).toDF("userId","movieId","rating")

    val conf = new SparkConf().setMaster("local[4]").setAppName("test")
    val sc = new SparkContext(conf)

    val ratingText = sc.textFile("/Users/hiwes/Downloads/推荐系统/数据包/MovieLens/ml-1m/ratings.dat")
    ratingText.first()
    val ratingRdd = ratingText.map(parseRating).cache()

    // 得到总共评分数
    println("Total number of ratings: " + ratingRdd.count())
    // 得到总共电影数
    println("Total number of movies: " + ratingRdd.map(_.movieId).distinct().count())
    // 得到总共用户数
    println("Total number of users: " + ratingRdd.map(_.userId).distinct().count())

    val spark = SparkSession.builder().config(conf).getOrCreate()
    import spark.implicits._

    val ratingDf = ratingRdd.toDF("user", "item", "rating")
    ratingDf.printSchema()
    ratingDf.show(false)

    val movieDf = sc.textFile("/Users/hiwes/Downloads/推荐系统/数据包/MovieLens/ml-1m/movies.dat")
      .map(parseMovie).toDF()
    movieDf.show(false)

    val userDf = sc.textFile("/Users/hiwes/Downloads/推荐系统/数据包/MovieLens/ml-1m/users.dat")
      .map(parseUser).toDF()
    userDf.show(false)

    //    // 注册SparkSQL临时表
    //    ratingDf.registerTempTable("ratings")
    //    movieDf.registerTempTable("movies")
    //    userDf.registerTempTable("users")

    // select title,rmax,rmin,ucnt from
    // (select movie, max(rating) as rmax, min(rating) as rmin, count(distinct(user)) as ucnt from ratings) ratingsCNT join movies on movie=movieId order by ucnt desc

    //    val result: DataFrame = spark.sql(
    //      //      "select title,rmax,rmin,ucnt " +
    //      //        "from (select movieId, max(rating) as rmax, min(rating) as rmin, count(distinct(userId)) as ucnt " +
    //      //        "from ratings group by movieId) ratingsCNT join movies on movieId = movieId order by ucnt desc"
    //      "select title, ramx, rmin, ucnt from (select movieId, max(rating) as rmax, min(rating) as rmin, count(distinct(userId)) as ucnt from ratings group by movieId) ratingsCNT join movies on product=movieId order by ucnt desc"
    //    )
    //    result.show(false)

    // ALS模型构建
    val Array(traing, test) = ratingDf.randomSplit(Array(0.8, 0.2), 0L)

    val als = new ALS()
      .setRegParam(0.01)
      .setRank(10)
      .setRank(20)

    val model = als.fit(traing)
    model.setColdStartStrategy("drop")

    val predictions: DataFrame = model.transform(test)
    predictions.show(false)

    val evaluator = new RegressionEvaluator()
      .setMetricName("mae")
      .setLabelCol("rating")
      .setPredictionCol("prediction")

    val mae = evaluator.evaluate(predictions)
    println(s"平均绝对误差MAE = $mae")

//    model.recommendForAllUsers(10).show(false)
  }

}
