package com.yangee.cores.project.IntelligentCustomerSystem.scala

/**
  * 9.偏应用函数。
  */
object pianyingyonghanshu9 {
  def main(args: Array[String]): Unit = {
    log("my is", "m1")
    log("my is", "m2")
    log("my is", "m3")

    val logWith = log("my is", _: String)

    logWith("m1")
    logWith("m2")
    logWith("m3")
  }

  def log(info: String, message: String) = {
    println(info + " " + message)
  }
}
