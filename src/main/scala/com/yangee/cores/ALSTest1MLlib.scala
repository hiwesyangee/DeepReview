package com.yangee.cores


import java.io.File

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.evaluation.RegressionMetrics
import org.apache.spark.mllib.recommendation.{MatrixFactorizationModel, Rating, ALS}
import org.apache.spark.rdd.RDD

import scala.util.Random

/**
  * 使用mllib库中的ALS算法进行推荐。
  */
object ALSTest1MLlib {
  //1. 定义一个评分函数
  def elicitateRating(movies: Seq[(Int, String)]) = {
    val prompt = "请给以下影片评分(1-5(最佳)或0(如未看过)):"
    println(prompt)
    val ratings = movies.flatMap { x =>
      var rating: Option[Rating] = None
      var valid = false
      while (!valid) {
        println(x._2 + " :")
        try {
          val r = Console.readInt()
          if (r > 5 || r < 0) {
            println(prompt)
          } else {
            valid = true
            if (r > 0) {
              rating = Some(Rating(0, x._1, r)) // Some是Option的一个子类，允许为空。
            }
          }
        } catch {
          case e: Exception => println(prompt)
        }
      }
      rating match {
        case Some(r) => Iterator(r)
        case None => Iterator.empty
      }
    }
    if (ratings.isEmpty) {
      error("No ratings provided!")
    } else {
      ratings
    }
  }

  //2. 定义一个RMSE计算函数
  def computeRmse(model: MatrixFactorizationModel, data: RDD[Rating]) = {
    val prediction: RDD[Rating] = model.predict(data.map(x => (x.user, x.product)))
    val realData: RDD[((Int, Int), Double)] = data.map(x => ((x.user, x.product), x.rating))
    val predDataJoined: RDD[(Double, Double)] = prediction.map(x => ((x.user, x.product), x.rating)).join(realData).values
    new RegressionMetrics(predDataJoined).rootMeanSquaredError
  }

  //3. Main
  def main(args: Array[String]) {
    //3.1 设置env
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

    if (args.length != 1) {
      print("Usage: movieLensHomeDir")
      sys.exit(1)
    }

    val conf = new SparkConf().setAppName("MovieLensALS")
      .setMaster("local[4]")
      .set("spark.executor.memory", "500m")
    val sc = new SparkContext(conf)

    //3.2 加载评级数据并了解数据
    val movieLensHomeDir = args(0)
    //    val movieLensHomeDir = "/Users/hiwes/Downloads/ml-latest-small/"
    val ratings: RDD[(Long, Rating)] = sc.textFile(new File(movieLensHomeDir, "ratings.dat").toString
    ).map { line =>
      val fields = line.split("::")
      //timestamp, user, product, rating
      (fields(3).toLong % 10, Rating(fields(0).toInt, fields(1).toInt, fields(2).toDouble))
    }
    val movies = sc.textFile(new File(movieLensHomeDir, "movies.dat").toString).map { line =>
      val fields = line.split("::")
      //movieId, movieName
      (fields(0).toInt, fields(1))
    }.collectAsMap()

    val numRatings = ratings.count()
    val numUser = ratings.map(x => x._2.user).distinct().count()
    val numMovie = ratings.map(_._2.product).distinct().count()

    println("获取 " + numRatings + " 条评分,从 " + numUser + " 个用户对 " + numMovie + " 部电影.")

    //3.3 激发个人评分
    val topMovies = ratings.map(_._2.product).countByValue().toSeq.sortBy(-_._2).take(50).map(_._1)
    val random = new Random(0)
    val selectMovies = topMovies.filter(x => random.nextDouble() < 0.2).map(x => (x, movies(x)))

    val myRatings = elicitateRating(selectMovies)
    val myRatingsRDD = sc.parallelize(myRatings, 1)

    //3.4 将数据分为训练(60%)、验证(20%)和测试(20%)
    val numPartitions = 10
    val trainSet = ratings.filter(x => x._1 < 6).map(_._2).union(myRatingsRDD).repartition(numPartitions).persist()
    val validationSet = ratings.filter(x => x._1 >= 6 && x._1 < 8).map(_._2).persist()
    val testSet = ratings.filter(x => x._1 >= 8).map(_._2).persist()

    val numTrain = trainSet.count()
    val numValidation = validationSet.count()
    val numTest = testSet.count()

    println("训练数据数: " + numTrain + " 验证数据数: " + numValidation + " 测试数据数: " + numTest)

    //3.5 用验证集对模型进行训练和优化
    val numRanks = List(8, 12)
    val numIters = List(10, 20)
    val numLambdas = List(0.1, 10.0)
    var bestRmse = Double.MaxValue
    var bestModel: Option[MatrixFactorizationModel] = None
    var bestRanks = -1
    var bestIters = 0
    var bestLambdas = -1.0
    for (rank <- numRanks; iter <- numIters; lambda <- numLambdas) {
      val model = ALS.train(trainSet, rank, iter, lambda)
      val validationRmse = computeRmse(model, validationSet)
      println("RMSE(validation) = " + validationRmse + " with ranks=" + rank + ", iter=" + iter + ", Lambda=" + lambda)

      if (validationRmse < bestRmse) {
        bestModel = Some(model)
        bestRmse = validationRmse
        bestIters = iter
        bestLambdas = lambda
        bestRanks = rank
      }
    }

    //3.6 在测试集上评估模型
    val testRmse = computeRmse(bestModel.get, testSet)
    println("最好的模型的rank= " + bestRanks + ", Iter= " + bestIters + ", Lambda= " + bestLambdas +
      " 在测试中计算RMSE= " + testRmse)

    //3.7 创建基准线并将其与最佳模型进行比较
    val meanRating = trainSet.union(validationSet).map(_.rating).mean() //得到平均评分
    val bestlineRmse = new RegressionMetrics(testSet.map(x => (x.rating, meanRating))).rootMeanSquaredError //创建基准线
    val improvement = (bestlineRmse - testRmse) / bestlineRmse * 100
    println("最好模型RMSE比基准线优化了: " + "%1.2f".format(improvement) + "%.")

    //3.8 进行个性化推荐
    val moviesId = myRatings.map(_.product)
    val candidates = sc.parallelize(movies.keys.filter(!moviesId.contains(_)).toSeq)
    val recommendations = bestModel.get
      .predict(candidates.map(x => (0, x)))
      .sortBy(-_.rating)
      .take(50)

    var i = 0
    println("推荐给你的电影:")
    recommendations.foreach { line =>
      println("%2d".format(i) + " :" + movies(line.product))
      i += 1
    }
    sc.stop()
  }
}

