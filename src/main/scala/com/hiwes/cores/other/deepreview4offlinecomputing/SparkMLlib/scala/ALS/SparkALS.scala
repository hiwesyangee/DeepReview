package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala.ALS

import org.apache.commons.math3.linear._

import org.apache.spark.sql.SparkSession

/**
  * ALS示例之:SparkALS.scala
  */
object SparkALS {
  // 通过命令行参数设置的参数
  var M = 0 // 电影总数
  var U = 0 // 用户总数
  var F = 0 // 特征总数
  var ITERATIONS = 0
  val LAMBDA = 0.01 // 正则化系数

  def generateR(): RealMatrix = {
    val mh = randomMatrix(M, F)
    val uh = randomMatrix(U, F)
    mh.multiply(uh.transpose())
  }

  def rmse(targetR: RealMatrix, ms: Array[RealVector], us: Array[RealVector]): Double = {
    val r = new Array2DRowRealMatrix(M, U)
    for (i <- 0 until M; j <- 0 until U) {
      r.setEntry(i, j, ms(i).dotProduct(us(j)))
    }
    val diffs = r.subtract(targetR)
    var sumSqs = 0.0
    for (i <- 0 until M; j <- 0 until U) {
      val diff = diffs.getEntry(i, j)
      sumSqs += diff * diff
    }
    math.sqrt(sumSqs / (M.toDouble * U.toDouble))
  }

  def update(i: Int, m: RealVector, us: Array[RealVector], R: RealMatrix): RealVector = {
    val U = us.length
    val F = us(0).getDimension
    var XtX: RealMatrix = new Array2DRowRealMatrix(F, F)
    var Xty: RealVector = new ArrayRealVector(F)
    // 给每个用户打分
    for (j <- 0 until U) {
      val u = us(j)
      // 添加 u * u^t 到 XtX
      XtX = XtX.add(u.outerProduct(u))
      // 添加 u * rating 到 Xty
      Xty = Xty.add(u.mapMultiply(R.getEntry(i, j)))
    }
    // 向对角项添加正则化系数
    for (d <- 0 until F) {
      XtX.addToEntry(d, d, LAMBDA * U)
    }
    // Solve it with Cholesky 其实是解一个A*x=b的方程
    new CholeskyDecomposition(XtX).getSolver.solve(Xty)
  }

  // 打印警告，表示这只是一个使用ALS的例子，最好还是使用ml包下的org.apache.spark.ml.recommendation.ALS进行更常规的用途。
  def showWarning() {
    System.err.println(
      """WARN: This is a naive implementation of ALS and is given as an example!
        |Please use org.apache.spark.ml.recommendation.ALS
        |for more conventional use.
      """.stripMargin)
  }

  def main(args: Array[String]) {

    var slices = 0

    val options = (0 to 4).map(i => if (i < args.length) Some(args(i)) else None)

    options.toArray match {
      case Array(m, u, f, iters, slices_) =>
        M = m.getOrElse("100").toInt
        U = u.getOrElse("500").toInt
        F = f.getOrElse("10").toInt
        ITERATIONS = iters.getOrElse("5").toInt
        slices = slices_.getOrElse("2").toInt
      case _ =>
        System.err.println("Usage: SparkALS [M] [U] [F] [iters] [partitions]")
        System.exit(1)
    }

    showWarning()

    println(s"Running with M=$M, U=$U, F=$F, iters=$ITERATIONS")

    val spark = SparkSession
      .builder
      .appName("SparkALS")
      .getOrCreate()

    val sc = spark.sparkContext

    val R = generateR()

    // 随机初始化m和u
    var ms = Array.fill(M)(randomVector(F))
    var us = Array.fill(U)(randomVector(F))

    // 迭代更新电影和用户
    val Rc = sc.broadcast(R)
    var msb = sc.broadcast(ms)
    var usb = sc.broadcast(us)
    for (iter <- 1 to ITERATIONS) {
      println(s"Iteration $iter:")
      ms = sc.parallelize(0 until M, slices)
        .map(i => update(i, msb.value(i), usb.value, Rc.value))
        .collect()
      msb = sc.broadcast(ms) // 重新广播ms因为它被更新了
      us = sc.parallelize(0 until U, slices)
        .map(i => update(i, usb.value(i), msb.value, Rc.value.transpose()))
        .collect()
      usb = sc.broadcast(us) // 重新广播us因为它被更新了
      println("RMSE = " + rmse(R, ms, us))
      println()
    }

    spark.stop()
  }

  private def randomVector(n: Int): RealVector =
    new ArrayRealVector(Array.fill(n)(math.random))

  private def randomMatrix(rows: Int, cols: Int): RealMatrix =
    new Array2DRowRealMatrix(Array.fill(rows, cols)(math.random))

}
