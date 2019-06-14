package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala.KMeans.mllib

import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.{SparkConf, SparkContext}

object KMeansTest {
  def main(args: Array[String]) {
    val conf = new SparkConf()
    val sc = new SparkContext(conf)

    // val data =sc.textFile(args(0))
    val data = sc.textFile("file:///Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/data/mllib/kmeans_data.txt")
    val parsedData = data.map(s => Vectors.dense(s.split(' ').map(_.trim.toDouble))).cache()

    //设置簇的个数为3————————k=3
    val numClusters = 3
    //迭代20次
    val numIterations = 20

    //设置初始K选取方式为k-means++
    val initMode = "k-means||"
    val model: KMeansModel = new KMeans()
      .setInitializationMode(initMode)
      .setK(numClusters)
      .setSeed(1L)
      .setMaxIterations(numIterations)
      .run(parsedData)


    //打印出测试数据属于哪个簇
    println(parsedData.map(v => v.toString() + " belong to cluster :" + model.predict(v)).collect().mkString("\n"))

    // 通过计算平方误差的集合和进行计算
    val WSSSE = model.computeCost(parsedData)
    println("WithinSet Sum of Squared Errors = " + WSSSE)

    val a21 = model.predict(Vectors.dense(1.2, 1.3))
    val a22 = model.predict(Vectors.dense(4.1, 4.2))

    //打印出中心点
    println("Clustercenters:")
    for (center <- model.clusterCenters) {
      println(" " + center)
    }

    println("Prediction of (1.2,1.3)-->" + a21)
    println("Prediction of (4.1,4.2)-->" + a22)
  }
}
