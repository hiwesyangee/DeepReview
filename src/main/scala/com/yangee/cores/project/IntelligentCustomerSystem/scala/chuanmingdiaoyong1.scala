package com.yangee.cores.project.IntelligentCustomerSystem.scala

/**
  * 1.函数传名调用。
  */
object chuanmingdiaoyong1 {
  def main(args: Array[String]): Unit = {
    delayed(time())
  }

  def time(): Long = {
    println("获取时间为纳秒：")
    System.nanoTime()
  }

  def delayed(t: Long): Long = {
    println("在delayed方法里面")
    println("参数为: " + t)
    return t
  }
}
