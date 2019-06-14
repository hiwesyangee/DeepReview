package com.yangee.cores

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.tuning.{ParamGridBuilder, TrainValidationSplit}

/**
  * 实例4:
  * 通过 训练校验分类 进行模型调优。
  */
object Test4 {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.apache.hbase").setLevel(Level.WARN)
    Logger.getLogger("org.apache.hadoop").setLevel(Level.WARN)
    Logger.getLogger("org.apache.zookeeper").setLevel(Level.WARN)

    val spark = SparkSession.builder().appName("Test").master("local[2]").getOrCreate()
    // 1.准备训练数据和测试数据；
    val data: DataFrame = spark.read.format("libsvm").load("sample_linear_regression_data.txt")
    val Array(traing, test) = data.randomSplit(Array(0.9, 0.1), seed = 12345) // 指定随机种子，还是有规律的
    // println(traing.count())
    // println(test.count())


    // 2.使用ParamGridBuilder构造一个参数网络；
    val lr = new LinearRegression()
    val paramGrid = new ParamGridBuilder()
      .addGrid(lr.elasticNetParam, Array(0.0, 0.5, 1.0))
      .addGrid(lr.fitIntercept) // 默认true/false。
      .addGrid(lr.regParam, Array(0.1, 0.01))
      .build()

    // 3.使用TrainValidationSplit来选择模型和参数；
    val trainValidationSplit = new TrainValidationSplit()
      .setEstimator(lr)
      .setEstimatorParamMaps(paramGrid)
      .setEvaluator(new RegressionEvaluator()) // 评估器
      .setTrainRatio(0.8) // 训练校验比例double

    // 4.运行训练校验校验，选择最好的参数集；
    val model = trainValidationSplit.fit(traing)

    // 5.在测试数据上做预测，模型是参数组合中执行最好的一个。
    println(test.count())
    model.transform(test).show(false)
  }
}
