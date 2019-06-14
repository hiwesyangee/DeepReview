package com.hiwes.cores.sparkmllib.classification

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{DecisionTreeClassificationModel, DecisionTreeClassifier}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}
import utils.SparkUtils

/**
  * 决策树算法。
  */
object DecisionTreeTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkUtils.getSparkSession()
    val data = spark.read.format("libsvm").load("/Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/data/mllib/sample_libsvm_data.txt")

    // 索引标签，向标签列添加元数据。
    // 适用于整个数据集，在索引中包含所有标签。
    val labelIndexer = new StringIndexer()
      .setInputCol("label")
      .setOutputCol("indexedLabel")
      .fit(data)
    // 自动识别分类特征，并对它们进行索引。
    val featureIndexer = new VectorIndexer()
      .setInputCol("features")
      .setOutputCol("indexedFeatures")
      .setMaxCategories(4) // 具有>4个不同值的特征被视为连续的。
      .fit(data)

    // 将数据划分为训练集和测试集(30%用于测试)。
    val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))

    // 训练决策树模型。
    val dt = new DecisionTreeClassifier()
      .setLabelCol("indexedLabel")
      .setFeaturesCol("indexedFeatures")

    // 将索引标签转换回原始标签。
    val labelConverter = new IndexToString()
      .setInputCol("prediction")
      .setOutputCol("predictedLabel")
      .setLabels(labelIndexer.labels)

    // 在管道中链接索引和树。
    val pipeline = new Pipeline()
      .setStages(Array(labelIndexer, featureIndexer, dt, labelConverter))

    // 训练模型。这也运行索引器。
    val model = pipeline.fit(trainingData)

    // 作出预测。
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

    val treeModel = model.stages(2).asInstanceOf[DecisionTreeClassificationModel]
    println("Learned classification tree model:\n" + treeModel.toDebugString)

    spark.stop()

  }
}
