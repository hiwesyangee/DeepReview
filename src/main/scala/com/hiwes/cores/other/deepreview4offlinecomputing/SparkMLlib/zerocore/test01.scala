package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.zerocore

import breeze.linalg.DenseVector
import org.apache.spark.ml.stat.Correlation
import org.apache.spark.mllib.linalg.{Matrices, Vector, Vectors}

object test01 {
  def main(args: Array[String]): Unit = {
    //    val v1: Vector = Vectors.dense(Array(1.0, 2.0, 3.0, 4.0))
    val v2: Vector = Vectors.dense(1.0, 2.0, 3.0, 4.0)

    val v3: DenseVector[Int] = breeze.linalg.DenseVector(1, 2, 3, 4)

    val v4 = v3 + v3 // 提供+符号的运算符重载
    val v5 = v3 * v3.t // .t代表转置

    val m1 = Matrices.dense(2, 3, Array(2, 3, 4, 5, 6, 7))
    val m2 = Matrices.dense(2, 3, Array(2, 5, 3, 6, 4, 7))

    println(m1)
    println(m2)

    val m3 = breeze.linalg.DenseMatrix(Array(1, 2, 3), Array(4, 5, 6))
    println("————————————————————————————————————————————————")
    println(m3)



    val m4 = breeze.linalg.DenseMatrix(Array(1,2,3,4,5,6)).reshape(2,3).t
    println(m4)

  }

}
