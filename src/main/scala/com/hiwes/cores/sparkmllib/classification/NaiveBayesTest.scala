package com.hiwes.cores.sparkmllib.classification

import org.apache.spark.ml.classification.NaiveBayes
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import utils.SparkUtils

/**
  * 朴素贝叶斯算法。
  */
object NaiveBayesTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkUtils.getSparkSession()

    // 加载并解析数据文件，将其转换为Dataframe。
    val data = spark.read.format("libsvm").load("/Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/data/mllib/sample_libsvm_data.txt")

    val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3), seed = 1234L)

    // 训练一个朴素贝叶斯模型。
    val model = new NaiveBayes()
      .fit(trainingData)

    // 选择要显示的示例行。
    val predictions = model.transform(testData)
    predictions.show()

    // 选择(预测，真实标签)并计算测试误差
    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("accuracy")
    val accuracy = evaluator.evaluate(predictions)
    println("Test set accuracy = " + accuracy)

    spark.stop()
  }
}
