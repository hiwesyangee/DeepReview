package com.hiwes.cores.sparkmllib.basicstatisticsAndcorrelationAndhypothesistesting

import org.apache.spark.mllib.linalg.Matrices
import org.apache.spark.mllib.stat.test.ChiSqTestResult
import org.apache.spark.mllib.{linalg, stat}

object ChiSquareTest {
  def main(args: Array[String]): Unit = {
    val data = Matrices.dense(2, 2, Array(127, 19, 147, 10))

    val result: ChiSqTestResult = stat.Statistics.chiSqTest(data)

    println(result)
    println(result.statistic)
    println(result.toString())
  }

}
