package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala.ALS

import org.apache.commons.math3.linear._

/**
  * ALS示例之:LocalALS.scala
  */
object LocalALS {

  // 通过命令行参数设置的参数
  var M = 0 // 电影总数
  var U = 0 // 用户总数
  var F = 0 // 特征总数
  var ITERATIONS = 0
  val LAMBDA = 0.01 // 正则化系数

  // 矩阵生成
  def generateR(): RealMatrix = {
    val mh = randomMatrix(M, F)
    val uh = randomMatrix(U, F)
    mh.multiply(uh.transpose())
  }

  // 求RMSE均方根误差
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

  // 更新电影Vec
  def updateMovie(i: Int, m: RealVector, us: Array[RealVector], R: RealMatrix): RealVector = {
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

  // 更新用户Vec
  def updateUser(j: Int, u: RealVector, ms: Array[RealVector], R: RealMatrix): RealVector = {
    var XtX: RealMatrix = new Array2DRowRealMatrix(F, F)
    var Xty: RealVector = new ArrayRealVector(F)
    // 对于用户评价的每一部电影遍历
    for (i <- 0 until M) {
      val m = ms(i)
      // 添加 m * m^t 到 XtX
      XtX = XtX.add(m.outerProduct(m))
      // 添加 m * rating 到 Xty
      Xty = Xty.add(m.mapMultiply(R.getEntry(i, j)))
    }
    // 向对角项添加正则化系数
    for (d <- 0 until F) {
      XtX.addToEntry(d, d, LAMBDA * M)
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
    args match {
      case Array(m, u, f, iters) =>
        M = m.toInt
        U = u.toInt
        F = f.toInt
        ITERATIONS = iters.toInt
      case _ =>
        System.err.println("Usage: LocalALS <M> <U> <F> <iters>")
        System.exit(1)
    }
    showWarning()
    println(s"Running with M=$M, U=$U, F=$F, iters=$ITERATIONS")
    val R = generateR()
    // 随机初始化m和u
    var ms = Array.fill(M)(randomVector(F))
    var us = Array.fill(U)(randomVector(F))

    // 迭代更新电影和用户
    for (iter <- 1 to ITERATIONS) {
      println(s"Iteration $iter:")
      ms = (0 until M).map(i => updateMovie(i, ms(i), us, R)).toArray
      us = (0 until U).map(j => updateUser(j, us(j), ms, R)).toArray
      println("RMSE = " + rmse(R, ms, us))
      println()
    }
  }

  // 数组充填
  private def randomVector(n: Int): RealVector =
    new ArrayRealVector(Array.fill(n)(math.random))

  // 矩阵充填
  private def randomMatrix(rows: Int, cols: Int): RealMatrix =
    new Array2DRowRealMatrix(Array.fill(rows, cols)(math.random))

}
