package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala.KMeans.mllib

import org.apache.spark.{SparkConf, SparkContext}

import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors

object KMeansExample {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("KMeansExample").setMaster("local[4]")
    val sc = new SparkContext(conf)

    // 加载和解析数据
    val data = sc.textFile("file:///Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/data/mllib/kmeans_data.txt")
    val parsedData = data.map(s => Vectors.dense(s.split(' ').map(_.toDouble))).cache()

    // 使用KMeans将数据集群到两个类中，设置k=2，迭代数为20
    val numClusters = 2
    val numIterations = 20
    val model: KMeansModel = KMeans.train(parsedData, numClusters, numIterations)
    //    KMeans.train(parsedData,numClusters,numIterations,"k-means||")
    //    KMeans.train(parsedData,numClusters,numIterations,"k-means||",1L)
    //    KMeans.train(parsedData,numClusters,numIterations,10)
    //    KMeans.train(parsedData,numClusters,numIterations,10,"k-means||")
    //    KMeans.train(parsedData,numClusters,numIterations,10,"k-means||",1L)

    //打印出测试数据属于哪个簇
    println(parsedData.map(v => v.toString() + " belong to cluster :" + model.predict(v)).collect().mkString("\n"))


    // 通过计算平方误差的集合和来评估聚类
    val WSSSE = model.computeCost(parsedData)
    println("Within Set Sum of Squared Errors = " + WSSSE)

    // 保存或下载模型
    model.save(sc, "/Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/target/org/apache/spark/KMeansExample/KMeansModel")
    val sameModel = KMeansModel.load(sc, "/Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/target/org/apache/spark/KMeansExample/KMeansModel")

    sc.stop()
  }

}
