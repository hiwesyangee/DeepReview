package com.hiwes.cores.sparkmllib.classification

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{RandomForestClassificationModel, RandomForestClassifier}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}
import utils.SparkUtils

/**
  * 随机森林算法。
  */
object RanndomForestTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkUtils.getSparkSession()

    // 加载并解析数据文件，将其转换为Dataframe。
    val data = spark.read.format("libsvm").load("/Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/data/mllib/sample_libsvm_data.txt")

    // 索引标签，向标签列添加元数据。适用于整个数据集，以包括所有标签的索引。
    val labelIndexer = new StringIndexer()
      .setInputCol("label")
      .setOutputCol("indexedLabel")
      .fit(data)
    // 自动识别分类特征，并对它们进行索引。设置maxCategories，使具有> 4个不同值的特性被视为连续的。
    val featureIndexer = new VectorIndexer()
      .setInputCol("features")
      .setOutputCol("indexedFeatures")
      .setMaxCategories(4)
      .fit(data)

    // 将数据划分为培训和测试集(30%的用于测试)。
    val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))

    // 训练一个随机森林模型。
    val rf = new RandomForestClassifier()
      .setLabelCol("indexedLabel")
      .setFeaturesCol("indexedFeatures")
      .setNumTrees(10)

    // 将索引标签转换回原始标签。
    val labelConverter = new IndexToString()
      .setInputCol("prediction")
      .setOutputCol("predictedLabel")
      .setLabels(labelIndexer.labels)

    // 链接索引器和管道中的forest。
    val pipeline = new Pipeline()
      .setStages(Array(labelIndexer, featureIndexer, rf, labelConverter))

    // 训练模型
    val model = pipeline.fit(trainingData)

    // 作出预测
    val predictions = model.transform(testData)

    // 选择要显示的示例行。
    predictions.select("predictedLabel", "label", "features").show(5)

    // 选择(预测，真实标签)并计算测试误差。
    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("indexedLabel")
      .setPredictionCol("prediction")
      .setMetricName("accuracy")
    val accuracy = evaluator.evaluate(predictions)
    println("Test Error = " + (1.0 - accuracy))

    val rfModel = model.stages(2).asInstanceOf[RandomForestClassificationModel]
    println("Learned classification forest model:\n" + rfModel.toDebugString)

    spark.stop()
  }
}
