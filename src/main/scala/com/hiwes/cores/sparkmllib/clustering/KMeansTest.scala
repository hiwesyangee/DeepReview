package com.hiwes.cores.sparkmllib.clustering

import org.apache.spark.ml.clustering.KMeans
import utils.SparkUtils
import org.apache.spark.ml.linalg
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.rdd.RDD

/**
  * KMeans算法。
  */
object KMeansTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkUtils.getSparkSession()
    //    val original: RDD[linalg.Vector] = spark.sparkContext.parallelize(List(
    //      Vectors.dense(0, 1, 2, 3, 4, 5),
    //      Vectors.dense(0, 1, 2, 3, 4, 6),
    //      Vectors.dense(1, 2, 5, 7, 8, 9),
    //      Vectors.dense(1, 2, 6, 7, 8, 9)
    //    ))
    val dataset = spark.read.textFile("/Users/hiwes/Downloads/kmeans/kMeans_demo/testSet.txt").toDF("features")

    val kmeans = new KMeans()
      .setK(2)
      .setInitMode("k-means||")
      .setFeaturesCol("features")
      .setSeed(1L)

    val model = kmeans.fit(dataset)
    val WSSSE: Double = model.computeCost(dataset) //集合内误差平方和（Within Set Sum of Squared Error, WSSSE)
    println(s"WSSSE: $WSSSE")

    model.clusterCenters.foreach(println)

    spark.stop()
  }
}
