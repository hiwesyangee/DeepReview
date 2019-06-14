package com.yangee.cores.project.IntelligentCustomerSystem.scala

/**
  * 5.默认参数值。
  */
object morencanshu5 {
  def main(args: Array[String]): Unit = {
    println(countA(b = 5))
  }

  def countA(a: Int = 5, b: Int) = {
    a + b
  }
}
