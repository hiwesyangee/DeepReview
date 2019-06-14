package com.hiwes.cores.sparkmllib.classification

import java.util.Random

import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Dataset
import utils.SparkUtils

/**
  * 决策树算法自写。
  */
object DecisionTreeTest4 {
  def main(args: Array[String]): Unit = {
    val spark = SparkUtils.getSparkSession()
    val sc = spark.sparkContext
    val data: RDD[String] = sc.textFile("/Users/hiwes/data/dt.data")
    val rand = new Random()

    val rdd: RDD[(Double, Double, Double, Double, Int, Double)] = data.map(row => {
      val arr = row.split("\t")
      val category = arr(4).toInt
      (arr(0).toDouble, arr(1).toDouble, arr(2).toDouble, arr(3).toDouble, category, rand.nextDouble())
    })

    val dataset = spark.createDataFrame(rdd)
      .toDF("book", "time", "vip", "islegal", "category", "rand").sort("rand")
    dataset.show(false)

    val assembler = new VectorAssembler()
      .setInputCols(Array("book", "time", "vip", "islegal"))
      .setOutputCol("features")

    val transformedDataset = assembler.transform(dataset)
    transformedDataset.show()

    val Array(train, test) = transformedDataset.randomSplit(Array(0.7, 0.3))

    val classifier = new DecisionTreeClassifier()
      .setFeaturesCol("features")
      .setLabelCol("category")
    val model = classifier.fit(train)
    val result = model.transform(test)
    result.show()

//    val evaluator = new MulticlassClassificationEvaluator()
//      .setLabelCol("category")
//      .setPredictionCol("prediction")
//      .setMetricName("accuracy")
//    val accuracy = evaluator.evaluate(result)
//    println(s"""accuracy is $accuracy""")
//
//    val evaluator2 = new MulticlassClassificationEvaluator()
//      .setLabelCol("category")
//      .setPredictionCol("prediction")
//      .setMetricName("weightedRecall")
//    val weightedRecall = evaluator2.evaluate(result)
//    println(s"""weightedRecall is $weightedRecall""")

  }
}
