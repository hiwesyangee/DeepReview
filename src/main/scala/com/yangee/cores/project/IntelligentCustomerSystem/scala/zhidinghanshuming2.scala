package com.yangee.cores.project.IntelligentCustomerSystem.scala

/**
  * 2.指定函数名。
  */
object zhidinghanshuming2 {
  def main(args: Array[String]): Unit = {
    println(printData(c = 10l, a = 12, b = "2"))
  }

  def printData(a: Int, b: String, c: Long): Unit = {
    a.toString + "  " + b + "  " + c.toString
  }
}
