package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala.KMeans.ml

import org.apache.spark.ml.clustering.{KMeans, KMeansModel}
import org.apache.spark.sql.SparkSession

/**
  * KMeans算法ML实现
  */
object KMeansExample {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName(s"${this.getClass.getSimpleName}")
      .master("local[4]")
      .getOrCreate()

    val dataset = spark.read.format("libsvm").load("file:///Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/data/mllib/sample_kmeans_data.txt")

    // 训练KMeans模型
    val kmeans = new KMeans().setK(2).setSeed(1L).setMaxIter(10).setInitMode("k-means||")
    val model: KMeansModel = kmeans.fit(dataset)

    // 通过计算平方误差的集合和来评估聚类————————————损失函数
    val WSSSE: Double = model.computeCost(dataset)
    println(s"Within Set Sum of Squared Errors = $WSSSE")

    println("Cluster Centers: ")
    model.clusterCenters.foreach(println)

    //    val momodel = new KMeans().setK(3).setSeed(1L).fit(dataset)
    //    val lf: Double = momodel.computeCost(dataset)
    //    println("Loss Function: " + lf)
    //
    //    model.setFeaturesCol("feature").setPredictionCol("prediction")
    //
    //    model.getFeaturesCol
    //    model.getPredictionCol

    spark.stop()
    //    Within Set Sum of Squared Errors = 0.11999999999994547
    //    Cluster Centers:
    //    [0.1,0.1,0.1]
    //    [9.1,9.1,9.1]
  }

}
