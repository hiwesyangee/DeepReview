package com.yangee.cores.project.IntelligentCustomerSystem.scala

/**
  * 7.嵌套函数。
  */
object qiantaohanshu7 {
  def main(args: Array[String]): Unit = {
    println(addData(4))
  }

  def addData(i: Int): Int = {
    def addData1(i: Int, j: Int = 7): Int = {
      val sum = i + j
      sum
    }

    addData1(i)
  }
}
