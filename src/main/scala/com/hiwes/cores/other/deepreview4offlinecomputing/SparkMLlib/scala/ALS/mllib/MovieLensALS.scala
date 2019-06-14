package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala.ALS.mllib

import com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala.AbstractParams

import scala.collection.mutable
import org.apache.log4j.{Level, Logger}
import scopt.OptionParser
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD

/**
  * org.apache.spark.mllib库的ALS算法实现
  *
  * 为什么说生产环境还是要使用spark.mllib库的算法实现
  * 为何不直接用spark.ml库的算法实现，直接使用DataFrame进行操作。
  * 将获取的结果进行进行存储即可。
  */
object MovieLensALS {

  // 定义所有的抽象参数
  case class Params(
                     input: String = null, //input表示数据路径
//                   input:String = "file:///Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/data/mllib/als/sample_movielens_ratings.txt",
                     kryo: Boolean = false, //kryo表示是否使用kryo序列化
                     numIterations: Int = 9, //numIterations表示迭代次数
                     lambda: Double = 0.1, //lambda表示正则化参数
                     rank: Int = 10, // rank表示
                     numUserBlocks: Int = -1, //numUserBlocks表示用户的分块数
                     numProductBlocks: Int = -1, //numProductBlocks表示物品的分块数
                     implicitPrefs: Boolean = false) //implicitPrefs这个参数没用过，但是通过后面的可以推断出来了，表示是否开启隐藏的分值参数阈值，进行线性加权。预测在那个级别才建议推荐，这里是5分制度的，详细看后面代码。
    extends AbstractParams[Params]

  /** 也就是说,所有的Scala类都可以使用这个作为常数类进行开发,使用case class Params(),在括号()中将所有
    * 常数进行初始化,然后继承AbstractParams[Params]即可。
    * 这个类原本是写在spark.example包中的,直接将其拷贝出重新写入到一个class中,导入包即可。 */

  def main(args: Array[String]) {
    val defaultParams = Params() // 调用抽象参数类，对默认函数进行初始化

    val parser = new OptionParser[Params]("MovieLensALS") {
      head("MovieLensALS: an example app for ALS on MovieLens data.")
      opt[Int]("rank") // 1.定义rank
        .text(s"rank, default: ${defaultParams.rank}")
        .action((x, c) => c.copy(rank = x))
      opt[Int]("numIterations") // 2.定义numIterations
        .text(s"number of iterations, default: ${defaultParams.numIterations}")
        .action((x, c) => c.copy(numIterations = x))
      opt[Double]("lambda") // 3.定义lambda
        .text(s"lambda (smoothing constant), default: ${defaultParams.lambda}")
        .action((x, c) => c.copy(lambda = x))
      opt[Unit]("kryo") // 4.定义kryo
        .text("use Kryo serialization")
        .action((_, c) => c.copy(kryo = true))
      opt[Int]("numUserBlocks") // 5.定义numUserBlocks
        .text(s"number of user blocks, default: ${defaultParams.numUserBlocks} (auto)")
        .action((x, c) => c.copy(numUserBlocks = x))
      opt[Int]("numProductBlocks") // 6.定义numProductBlocks
        .text(s"number of product blocks, default: ${defaultParams.numProductBlocks} (auto)")
        .action((x, c) => c.copy(numProductBlocks = x))
      opt[Unit]("implicitPrefs") // 7.定义implicitPrefs
        .text("use implicit preference")
        .action((_, c) => c.copy(implicitPrefs = true))
      arg[String]("<input>")
        .required()
        .text("input paths to a MovieLens dataset of ratings")
        .action((x, c) => c.copy(input = x))
      note(
        """
          |For example, the following command runs this app on a synthetic dataset:
          |
          | bin/spark-submit --class org.apache.spark.examples.mllib.MovieLensALS \
          |  examples/target/scala-*/spark-examples-*.jar \
          |  --rank 5 --numIterations 20 --lambda 1.0 --kryo \
          |  data/mllib/sample_movielens_data.txt
        """.stripMargin)
    }

    parser.parse(args, defaultParams) match {
      case Some(params) => run(params) // 运行建模方法run(),进行参数建模
      case _ => sys.exit(1) // 0为正常退出，其他数值（1-127）为不正常，可抛异常事件供捕获，但还是进行退出。
    }
  }

  def run(params: Params): Unit = {
    val conf = new SparkConf().setAppName(s"MovieLensALS with $params")
    if (params.kryo) { //将kryo默认定义为true，进行kryo序列化。
      conf.registerKryoClasses(Array(classOf[mutable.BitSet], classOf[Rating])) //对自定义类进行kryo注册
        .set("spark.kryoserializer.buffer", "8m")
    }
    val sc = new SparkContext(conf)

    Logger.getRootLogger.setLevel(Level.WARN) // 定义日志打印输出级别

    val implicitPrefs = params.implicitPrefs

    val ratings = sc.textFile(params.input).map { line =>
      val fields = line.split("::")
      if (implicitPrefs) {
        /*
         * MovieLens ratings are on a scale of 1-5:
         * 5: 必看
         * 4: 好看
         * 3: 还不错
         * 2: 相当糟糕
         * 1: 极其可怕
         * 所以不应该推荐预测评级低于3的电影。
         * To map ratings to confidence scores, we use
         * 5 -> 2.5, 4 -> 1.5, 3 -> 0.5, 2 -> -0.5, 1 -> -1.5. This mappings means unobserved
         * entries are generally between It's okay and Fairly bad.
         * 这种映射意味着未被观察到的条目通常介于ok和相当糟糕之间。
         *
         * The semantics of 0 in this expanded world of non-positive weights
         * are "the same as never having interacted at all".
         * 在这个非正权重的扩展世界中，0的语义“就像从未交互一样”。
         */
        /** 开启隐藏的分值参数阈值 == true,表示进行线性加权————————
          * 所有评分在原本基础减去了2.5，进行线性加权。 */
        Rating(fields(0).toInt, fields(1).toInt, fields(2).toDouble - 2.5)
      } else {
        Rating(fields(0).toInt, fields(1).toInt, fields(2).toDouble)
      }
    }.cache()

    // 对评分数，用户数，物品数进行去重计数
    val numRatings = ratings.count()
    val numUsers = ratings.map(_.user).distinct().count()
    val numMovies = ratings.map(_.product).distinct().count()

    println(s"Got $numRatings ratings from $numUsers users on $numMovies movies.")

    val splits = ratings.randomSplit(Array(0.8, 0.2))
    val training = splits(0).cache()
    val test = if (params.implicitPrefs) {
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
      splits(1).map(x => Rating(x.user, x.product, if (x.rating > 0) 1.0 else 0.0))
    } else {
      splits(1)
    }.cache()

    val numTraining = training.count()
    val numTest = test.count()
    println(s"Training: $numTraining, test: $numTest.") // 打印训练数据量和测试数据量

    ratings.unpersist(blocking = false)

    // 使用默认参数进行建模,其关键在于参数的设置，包含:rank,iter,lambda,usersBlock,itemsBlock,并且在最后使用run()进行验证。
    val model = new ALS()
      .setRank(params.rank)
      .setIterations(params.numIterations)
      .setLambda(params.lambda)
      .setImplicitPrefs(params.implicitPrefs)
      .setUserBlocks(params.numUserBlocks)
      .setProductBlocks(params.numProductBlocks)
      .run(training) // 使用run()进行验证，并且在下方进行调用RMSE计算方法，得到RMSE。

    val rmse = computeRmse(model, test, params.implicitPrefs)
    println(s"Test RMSE = $rmse.")

    // 原本程序在这里已经结束，通过对模型进行调用API，直接进行定义数目的推荐，并且结果为一个RDD，通过对RDD进行操作
    // 获取推荐的结果为：tup._1。
    val userRecs: RDD[(Int, Array[Rating])] = model.recommendProductsForUsers(10)
    val itemRecs: RDD[(Int, Array[Rating])] = model.recommendUsersForProducts(10)

    userRecs.collect().foreach(tup => {
      println("对用户" + tup._2(1) + s"的推荐结果为:${tup._1}")
    })

    itemRecs.collect().foreach(tup => {
      println("对物品" + tup._2(1) + s"对物品的推荐结果为:${tup._2}")
    })


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
