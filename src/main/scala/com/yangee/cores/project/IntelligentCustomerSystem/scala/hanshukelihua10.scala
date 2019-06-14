package com.yangee.cores.project.IntelligentCustomerSystem.scala

/**
  * 10.函数柯里化
  * 将原来接收两个参数的函数变成新的接收一个参数的函数的过程。新的函数返回一个以原有第二个参数为参数的函数
  */
object hanshukelihua10 {
  def main(args: Array[String]): Unit = {
    println(add(2, 3))
    println(rString("wo shi ")("bigdata ml"))
  }

  def add(x: Int, y: Int) = x + y

  def rString(s1: String)(s2: String) = s1 + s2

}
