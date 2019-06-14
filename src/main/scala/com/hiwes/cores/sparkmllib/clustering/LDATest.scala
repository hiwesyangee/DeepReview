package com.hiwes.cores.sparkmllib.clustering

import java.util.Random

import org.apache.spark.ml.clustering.LDA
import org.apache.spark.ml.feature.VectorAssembler
import utils.SparkUtils

/**
  * LDA算法。
  */
object LDATest {
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

    val lda = new LDA().setK(3).setMaxIter(20)
    val model = lda.fit(train)
    val prediction = model.transform(train)

    val ll = model.logLikelihood(train)  // 最大似然估计   越大越好
    val lp = model.logPerplexity(train)  // 困难度   越小越好

    val topics = model.describeTopics(3)
    prediction.select("label", "topicDistribution").show(false)
    topics.show(false)

    println("ll: " + ll)
    println("lp: " + lp)

  }
}
