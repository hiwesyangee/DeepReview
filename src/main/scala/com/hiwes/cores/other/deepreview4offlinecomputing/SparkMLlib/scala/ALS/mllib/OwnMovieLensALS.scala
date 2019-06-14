package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala.ALS.mllib

import scala.collection.mutable
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD

object OwnMovieLensALS {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("MovieLensALS").setMaster("local[4]")
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .registerKryoClasses(Array(classOf[mutable.BitSet], classOf[Rating])) //对自定义类进行kryo注册
      .set("spark.kryoserializer.buffer", "8m")

    val sc = new SparkContext(conf)
    //    Logger.getRootLogger.setLevel(Level.WARN)
    // 定义日志打印输出级别
    Logger.getRootLogger.setLevel(Level.ERROR)

    val path = "file:///Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/data/mllib/als/sample_movielens_ratings.txt"
    val ratings = sc.textFile(path).map { line =>
      val fields = line.split("::")
      Rating(fields(0).toInt, fields(1).toInt, fields(2).toDouble - 2.5)
    }.cache()

    // 对评分数，用户数，物品数进行去重计数
    val numRatings = ratings.count()
    val numUsers = ratings.map(_.user).distinct().count()
    val numMovies = ratings.map(_.product).distinct().count()

    println(s"Got $numRatings ratings from $numUsers users on $numMovies movies.")
    val splits = ratings.randomSplit(Array(0.8, 0.2))
    val training = splits(0).cache()
    /**
      * 0 means "don't know" and positive values mean "confident that the prediction should be 1".
      * Negative values means "confident that the prediction should be 0".
      * We have in this case used some kind of weighted RMSE. The weight is the absolute value of
      * the confidence. The error is the difference between prediction and either 1 or 0,
      * depending on whether r is positive or negative.
      * 0表示“不知道”，正值表示“确信预测应该是1”。负值表示“确信预测应该为0”。
      * 在这种情况下我们使用了某种加权RMSE。权重是置信度的绝对值。误差是预测和1或0之间的差值，取决于r是正还是负。
      */
    // 对分数进行判定，因为进行了线性加权，所以对小于0的数字进行增加权重，小于0的变成0分，表示不喜欢。
    val test = splits(1).map(x => Rating(x.user, x.product, if (x.rating > 0) 1.0 else 0.0)).cache()

    val numTraining = training.count()
    val numTest = test.count()
    println(s"Training: $numTraining, test: $numTest.") // 打印训练数据量和测试数据量

    ratings.unpersist(blocking = false)

    // 使用默认参数进行建模,其关键在于参数的设置，包含:rank,iter,lambda,usersBlock,itemsBlock,并且在最后使用run()进行验证。
    val model = new ALS()
      .setRank(5)
      .setIterations(9)
      .setLambda(0.1)
      .setImplicitPrefs(true)
      .setUserBlocks(-1)
      .setProductBlocks(-1)
      .run(training) // 使用run()进行验证，并且在下方进行调用RMSE计算方法，得到RMSE。

    // 原本程序在这里已经结束，通过对模型进行调用API，直接进行定义数目的推荐，并且结果为一个RDD，通过对RDD进行操作
    // 获取推荐的结果为：tup._1。
    val userRecs: RDD[(Int, Array[Rating])] = model.recommendProductsForUsers(20)
    val itemRecs: RDD[(Int, Array[Rating])] = model.recommendUsersForProducts(20)

    userRecs.collect().foreach(tup => {
      var result = ""
      for (s <- tup._2) {
        result += s.product + ","
      }
      val enen = result.substring(0, result.length - 1)
      println("对用户" + tup._1 + s" 的推荐结果为:$enen")
    })


    itemRecs.collect().foreach(tup => {
      var result = ""
      for (s <- tup._2) {
        result += s.user + ","
      }
      val enen = result.substring(0, result.length - 1)
      println("对物品" + tup._1 + s" 的推荐结果为:$enen")
    })


    //    for (s <- userRecs.collect()) {
    //      println("用户是：" + s._1)
    //
    //      var result = ""
    //      for (r <- s._2) {
    //        result += r.product + ","
    //      }
    //      val enen = result.substring(0, result.length - 1)
    //      println("推荐结果是：" + enen)
    //    }

    val rmse = computeRmse(model, test, true)
    println(s"Test RMSE = $rmse.")

    sc.stop()
  }

  /** 计算RMSE(均方根误差) */
  def computeRmse(model: MatrixFactorizationModel, data: RDD[Rating], implicitPrefs: Boolean): Double = {

    def mapPredictedRating(r: Double): Double = {
      if (implicitPrefs) math.max(math.min(r, 1.0), 0.0) else r
    }

    val predictions: RDD[Rating] = model.predict(data.map(x => (x.user, x.product)))
    val pretictionsAndRatings1: RDD[((Int, Int), Double)] = predictions.map { x =>
      ((x.user, x.product), mapPredictedRating(x.rating))
    }

    val predictionAndRatings2: RDD[((Int, Int), Double)] = data.map(x => ((x.user, x.product), x.rating))

    val predictionsAndRatings = pretictionsAndRatings1.join(predictionAndRatings2).values

    math.sqrt(predictionsAndRatings.map(x => (x._1 - x._2) * (x._1 - x._2)).mean())
  }
}
