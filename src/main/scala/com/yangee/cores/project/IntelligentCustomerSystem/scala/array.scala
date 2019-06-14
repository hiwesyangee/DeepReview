package com.yangee.cores.project.IntelligentCustomerSystem.scala

import Array._

/**
  * 数组相关
  */
object array {
  def main(args: Array[String]): Unit = {
    var arr = Array(1, 2, 3, 4, 5)

    var arr0 = new Array[String](3)

    // 创建多维数组
    val metrix: Array[Array[Int]] = ofDim[Int](3, 3)

    for (i <- 0 to 2)
      for (k <- 0 to 2)
        metrix(i)(k) = k + 3

    for (i <- 0 to 2) {
      for (k <- 0 to 2) {
        print(" " + metrix(i)(k))
      }
      println()
    }

    // 数组操作
    val arr1 = Array(1, 2, 3, 4)
    val arr2 = Array(5, 6, 7, 8)

    // 数组合并
    val hebingArr = concat(arr1, arr2)
    val hebingArr2 = arr1 ++ arr2

    // 创建区间数组
    val range1: Array[Int] = range(1, 10, 2)
    for(a<-range1){
      println(a)
    }
    //




  }
}
