package com.hiwes.cores.sparkmllib.basicstatisticsAndcorrelationAndhypothesistesting

import org.apache.spark.mllib.linalg
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.stat._
import org.apache.spark.rdd.RDD
import utils.SparkUtils

/**
  * 基础统计，包含:最大值，最小值，平均值，个数，1-范数，2-范数，非零数，方差，中位数，众数...
  * 用处: 在Spark进行模型训练之前，了解数据集的总体情况。
  */
object BasicStaticsTest {
  def main(args: Array[String]): Unit = {
    val sc = SparkUtils.getSparkContext()

    val line = sc.textFile("basic.txt")
    val dataVec: RDD[linalg.Vector] = line.flatMap(_.split(",")).map(value => {
      Vectors.dense(value.toDouble)
    })

    val result: MultivariateStatisticalSummary = Statistics.colStats(dataVec)
    println("最小值: "+result.min)
    println("最大值: "+result.max)
    println("平均值: "+result.mean)
    println(" 个数 : "+result.count)
    println("1-范数: "+result.normL1)
    println("2-范数: "+result.normL2)  // 模长
    println("非零数: "+result.numNonzeros)
    println(" 方差 : "+result.variance)

  }
}
