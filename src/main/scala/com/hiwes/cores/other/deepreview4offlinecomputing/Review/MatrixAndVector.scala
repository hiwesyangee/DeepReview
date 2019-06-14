package com.hiwes.cores.other.deepreview4offlinecomputing.Review

import breeze.linalg._
import breeze.stats._
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.linalg.{Vector, _}
import org.apache.spark.mllib.stat._
import org.apache.spark.rdd.RDD

/**
  * 复习:矩阵Matrix和向量Vector
  */
object MatrixAndVector {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("Test")
    val sc = new SparkContext(conf)

    /** 统计 */
    //    val observations = sc.parallelize(
    //      Seq(
    //        Vectors.dense(1.0, 0.1.0, 0.10.0),
    //        Vectors.dense(2.0, 0.2.0, 0.20.0),
    //        Vectors.dense(3.0, 0.3.0, 0.30.0)
    //      )
    //    )
    //
    //    // 计算列汇总统计信息。
    //    val summary: MultivariateStatisticalSummary = Statistics.colStats(observations)
    //    println(summary.mean)  // 包含每一列的平均值的密集向量
    //    println(summary.variance)  // 列方差
    //    println(summary.numNonzeros)  // 每个列中的非零数

    //    val line: String = "1,2,3,4,5,6"
    //    val dataVec: RDD[Vector] = sc.parallelize(line.split(",")).map(value => Vectors.dense(value.toDouble))
    //    val result: MultivariateStatisticalSummary = org.apache.spark.mllib.stat.Statistics.colStats(dataVec)
    //    println(result.mean)

    /** 相关系数 */
    //    val data1 = sc.parallelize(Array(1.0,3.0,5.0,6.0,7.0))
    //    val data2 = sc.parallelize(Array(3.0,1.0,0.1.0,11.0,2.0))
    //    val data3 = sc.parallelize(Array(1.0,2.0,3.0,4.0,5.0))
    //    val data4 = sc.parallelize(Array(3.0,4.0,5.0,6.0,7.0))
    //
    //    val result1: Double = Statistics.corr(data1,data2,"pearson")
    //    val result2: Double = Statistics.corr(data1,data2,"pearson")
    //    val result3: Double = Statistics.corr(data1,data3,"pearson")
    //    val result4: Double = Statistics.corr(data1,data4,"pearson")
    //    println(result1)
    //    println(result2)
    //    println(result3)
    //    println(result4)

    val line0 = "1,1,0.2:1,2,19:2,1,19:3,2,22:4,1,23:5,1,0.2"
    val line = "1,0.1,1,0.1,20:1,0.1,2,0.3,19:2,0.1,1,0.4,19:3,0.5,2,0.4,22:4,0.1,1,0.2,23:5,0.5,1,0.3,17"
    val data = sc.parallelize(line.split(":")) // 1,1,0.2

    val vec: RDD[Vector] = data.map(x => {
      Vectors.dense(x.split(",").map(x => x.toDouble))
    })

    val model: KMeansModel = KMeans.train(vec, 5, 5)
    vec.foreach(s => {
      val result = model.predict(s)
      println(s + "========" + result)
    })

    val result: Double = model.computeCost(vec)
    println(result)
  }

}
