package com.yangee.cores

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.{HashingTF, Tokenizer}
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.tuning.{CrossValidator, CrossValidatorModel, ParamGridBuilder}
import org.apache.spark.sql.SparkSession

/**
  * 实例3:
  * 模型调优。基于整个Pipeline。
  */
object Test3 {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.apache.hbase").setLevel(Level.WARN)
    Logger.getLogger("org.apache.hadoop").setLevel(Level.WARN)
    Logger.getLogger("org.apache.zookeeper").setLevel(Level.WARN)

    val spark = SparkSession.builder().appName("Test").master("local[2]").getOrCreate()
    // 1.准备训练数据
    val training = spark.createDataFrame(Seq(
      (0L, "a b c d e spark", 1.0),
      (1L, "b d", 0.0),
      (2L, "spark f g h", 1.0),
      (3L, "hadoop mapreduce", 0.0),
      (4L, "b spark who", 1.0),
      (5L, "g d a y", 0.0),
      (6L, "spark fly", 1.0),
      (7L, "was mapreduce", 0.0),
      (8L, "e spark program", 1.0),
      (9L, "a e c l", 0.0),
      (10L, "spark complie", 1.0),
      (11L, "hadoop software", 0.0)
    )).toDF("id", "text", "label")

    // 2.配置ML管道，包含三个Stage: Tokenizer，HashingTF和LR
    val tokenizer = new Tokenizer()
      .setInputCol("text")
      .setOutputCol("words")
    val hashingTF = new HashingTF()
      // .setNumFeatures(100) //文本量很大的时候一定要指定特征数量
      .setInputCol(tokenizer.getOutputCol)
      .setOutputCol("features")
    val lr = new LogisticRegression().setMaxIter(10) // 此例主要为了调优正则化系数
    val pipeline = new Pipeline().setStages(Array(tokenizer, hashingTF, lr))

    // 3.使用ParamGridBuilder构造一个参数网络
    val paramGrid = new ParamGridBuilder()
      .addGrid(hashingTF.numFeatures, Array(10, 100, 1000))
      .addGrid(lr.regParam, Array(0.1, 0.01)).build()

    // 4.使用CrossValidator来选择模型和参数
    // CrissValidator需要以个Estimator，一个评估器参数集合，和一个Evaluator
    val cv = new CrossValidator()
      .setEstimator(pipeline)
      .setEstimatorParamMaps(paramGrid)
      .setEvaluator(new BinaryClassificationEvaluator())
      .setNumFolds(2)

    // 5.运行交叉校验，选择最好的参数集
    val cvModel: CrossValidatorModel = cv.fit(training)

    println(cvModel.extractParamMap())

    // 6.准备测试数据
    val test = spark.createDataFrame(Seq(
      (12L, "spark h d e"),
      (13L, "a f c"),
      (14L, "mapreduce spark"),
      (15L, "apache hadoop")
    )).toDF("id", "text")

    // 7.预测结果
    cvModel.transform(test).show(false)

  }
}
