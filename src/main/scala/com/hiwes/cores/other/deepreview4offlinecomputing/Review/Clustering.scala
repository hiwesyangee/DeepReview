package com.hiwes.cores.other.deepreview4offlinecomputing.Review

import java.util.Random

import org.apache.spark.SparkConf
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.{Dataset, SparkSession}

/**
  * 聚类算法
  */
object Clustering {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("Test")
    val spark = SparkSession.builder().config(conf).getOrCreate()
    spark.sparkContext.setLogLevel("WARN") // 日志级别

    import spark.implicits._
    val file = spark.read.format("csv").load("iris.data")

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
//    train.show()

    val kmeans = new KMeans().setFeaturesCol("features").setK(3).setMaxIter(20)
    val model = kmeans.fit(train)
    model.transform(train).show()

    val result: Double = model.computeCost(dataset)
    println(result)
  }

}
