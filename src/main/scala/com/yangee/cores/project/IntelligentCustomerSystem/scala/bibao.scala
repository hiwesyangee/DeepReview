package com.yangee.cores.project.IntelligentCustomerSystem.scala

/**
  * 闭包函数。
  * 返回值依赖于生命在函数外部的一个或多个变量.
  */
object bibao {
  def main(args: Array[String]): Unit = {
    println(multiplier(22))
  }

  var j = 4

  val multiplier = (i: Int) => i * j

}
