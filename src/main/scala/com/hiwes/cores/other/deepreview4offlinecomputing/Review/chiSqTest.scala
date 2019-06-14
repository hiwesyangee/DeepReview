package com.hiwes.cores.other.deepreview4offlinecomputing.Review

import org.apache.spark.mllib.stat.test.ChiSqTestResult

/**
  * 测试Pearson卡方检验
  */
object chiSqTest {
  def main(args: Array[String]): Unit = {
    import org.apache.spark.mllib.{linalg, stat}
    val data = linalg.Matrices.dense(2, 2, Array(127, 10, 147, 10))
    val result: ChiSqTestResult = stat.Statistics.chiSqTest(data)
    println(result)
    if(result.pValue > 0.05){
      println("性别与左撇子有关系")
    }else{
      println("性别与左撇子无关系")
    }
  }

}
