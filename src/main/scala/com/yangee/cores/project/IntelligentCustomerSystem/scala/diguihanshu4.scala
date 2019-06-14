package com.yangee.cores.project.IntelligentCustomerSystem.scala

/**
  * 4.递归函数。
  */
object diguihanshu4 {
  def main(args: Array[String]): Unit = {
    println(att(10))
  }


  def att(n: Int): Int = {
    if (n < 0) {
      -1
    } else {
      att(n - 1)
    }
  }
}
