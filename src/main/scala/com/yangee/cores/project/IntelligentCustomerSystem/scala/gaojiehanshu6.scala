package com.yangee.cores.project.IntelligentCustomerSystem.scala

/**
  * 6.高阶函数。
  */
object gaojiehanshu6 {
  def main(args: Array[String]): Unit = {
    println(apply(layout, 20))
  }

  def apply(f: Int => String, v: Int): String = f(v)

  def layout[A](x: A) = "[" + x.toString() + "]"

}
