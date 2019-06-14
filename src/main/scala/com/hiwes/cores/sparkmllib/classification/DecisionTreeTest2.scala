package com.hiwes.cores.sparkmllib.classification

import java.util.Random

import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.VectorAssembler
import utils.SparkUtils

/**
  * 决策树算法。
  */
object DecisionTreeTest2 {
  def main(args: Array[String]): Unit = {
    val spark = SparkUtils.getSparkSession()

    val file = spark.read.format("csv").load("/Users/hiwes/Downloads/coding-271/ch9/cluster_lda/cluster/iris.data")
    file.show()

    import spark.implicits._
    val random = new Random()
    val data = file.map(row => {
      val label = row.getString(4) match {
        case "Iris-setosa" => 0
        case "Iris-versicolor" => 1
        case "Iris-virginica" => 2
      }
      (row.getString(0).toDouble,
        row.getString(1).toDouble,
        row.getString(2).toDouble,
        row.getString(3).toDouble,
        label,
        random.nextDouble())
    }).toDF("_c0", "_c1", "_c2", "_c3", "label", "rand").sort("rand")

    val assembler = new VectorAssembler()
      .setInputCols(Array("_c0", "_c1", "_c2", "_c3"))
      .setOutputCol("features")

    val dataset = assembler.transform(data)
    val Array(train, test) = dataset.randomSplit(Array(0.8, 0.2))
    train.show(false)

    val dt = new DecisionTreeClassifier().setFeaturesCol("features").setLabelCol("label")
    val model = dt.fit(train)

    val result = model.transform(train)

    val evalutor = new MulticlassClassificationEvaluator()
      .setLabelCol("label")
      .setPredictionCol("prediction")
      .setMetricName("accuracy")
    val accuracy = evalutor.evaluate(result)
    println(s"accuracy: $accuracy")
  }
}
