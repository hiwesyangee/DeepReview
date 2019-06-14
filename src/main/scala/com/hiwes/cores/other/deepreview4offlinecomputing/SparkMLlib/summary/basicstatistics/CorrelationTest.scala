package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.summary.basicstatistics

import org.apache.spark.ml.linalg.{Matrix, Vectors}
import org.apache.spark.ml.stat.Correlation
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
  * 基本统计之:相关性示例
  */
object CorrelationTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("CorrelationExample")
      .master("local[4]")
      .getOrCreate()
    import spark.implicits._

    val str1 = "14,25,30,45,56,61" // 1.0,2.0,3.0,4.0,5.0,6.0
    val str2 = "4,26,55,66,81,84" // 0.0,2.0,5.0,6.0,8.0,8.0

    val arr1 = str1.split(",")
    val arr2 = str2.split(",")
    val enen1: Array[Double] = arr1.map(x => {
      (x.toInt / 10).toDouble
    })

    val enen2: Array[Double] = arr2.map(x => {
      (x.toInt / 10).toDouble
    })



    val vec1 = Vectors.dense(enen1)
    val vec2 = Vectors.dense(enen2)

    val data = Seq(vec1, vec2)


    val df = data.map(Tuple1.apply).toDF("features")

    val row: DataFrame = Correlation.corr(df, "features")
    row.show(false)

    //    val Row(coeff1: Matrix) = row.head
    //    println("Pearson correlation matri、x:\n" + coeff1.toString)
    //    val Row(coeff1: Matrix) = Cor、relation.corr(df, "features").head
    //
    //    println("Pearson correlation matrix:\n" + coeff1.toString)

  }
}
