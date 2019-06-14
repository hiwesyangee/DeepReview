package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala.KMeans.ml

import org.apache.spark.ml.clustering.BisectingKMeans
import org.apache.spark.ml.linalg
import org.apache.spark.sql.SparkSession

object BisectingKMeansExample {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("BisectingKMeansExample")
      .master("local[4]")
      .getOrCreate()

    val dataset = spark.read.format("libsvm").load("file:///Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/data/mllib/sample_kmeans_data.txt")

    // 训练一个二分k-means模型。
    val bkm = new BisectingKMeans().setK(2).setSeed(1)
    val model = bkm.fit(dataset)

    // 评价聚类
    val cost = model.computeCost(dataset)
    println(s"Within Set Sum of Squared Errors = $cost")

    // 显示结果
    println("Cluster Centers: ")
    model.clusterCenters.foreach(println)

    spark.stop()
    //    Within Set Sum of Squared Errors = 0.11999999999994547
    //    Cluster Centers:
    //    [0.1,0.1,0.1]
    //    [9.1,9.1,9.1]
  }

}
