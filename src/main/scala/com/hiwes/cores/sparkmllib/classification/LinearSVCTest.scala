package com.hiwes.cores.sparkmllib.classification

import org.apache.spark.ml.classification.LinearSVC
import utils.SparkUtils

object LinearSVCTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkUtils.getSparkSession()

    val training = spark.read.format("libsvm").load("/Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/data/mllib/sample_libsvm_data.txt")

    val lsvc = new LinearSVC()
      .setMaxIter(10)
      .setRegParam(0.1)

    val lsvcModel = lsvc.fit(training)

    // 打印线性svc的系数和截距
    println(s"Coefficients: ${lsvcModel.coefficients} Intercept: ${lsvcModel.intercept}")

    spark.stop()
  }

}
