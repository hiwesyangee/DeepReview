package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala.KMeans.mllib

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.clustering.BisectingKMeans
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.rdd.RDD

object BisectingKMeansExample {
  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName(s"${this.getClass.getSimpleName}").setMaster("local[4]")
    val sc = new SparkContext(sparkConf)

    // 加载和解析数据
    def parse(line: String): Vector = Vectors.dense(line.split(" ").map(_.toDouble))

    val data: RDD[Vector] = sc.textFile("file:///Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/data/mllib/kmeans_data.txt").map(parse).cache()

    // 通过平分方法将数据聚类到6个集群中。k=6
    val bkm = new BisectingKMeans().setK(6).setSeed(1L).setMaxIterations(10)
    val model = bkm.run(data)

    //打印出测试数据属于哪个簇
    println(data.map(v => v.toString() + " belong to cluster :" + model.predict(v)).collect().mkString("\n"))


    // 显示损失函数值和类簇中心点
    println(s"Compute Cost: ${model.computeCost(data)}")

    model.clusterCenters.zipWithIndex.foreach { case (center, idx) =>
      println(s"Cluster Center ${idx}: ${center}")
    }

    sc.stop()
  }

}
