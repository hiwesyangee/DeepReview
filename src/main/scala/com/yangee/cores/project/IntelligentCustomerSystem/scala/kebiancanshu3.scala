package com.yangee.cores.project.IntelligentCustomerSystem.scala

/**
  * 3.可变参数。
  */
object kebiancanshu3 {
  def main(args: Array[String]): Unit = {
    println(printString(22, "china", "uk", "ak", "wk"))
//    println(printInt("end",11,22,33,44))
  }

  def printString(firstData: Int, args: String*): Unit = {
    println("firstData = " + firstData)
    var i = 0
    for (arg <- args) {
      println("Arg value[" + i + "]:" + arg)
      i = i + 1
    }
  }

  def printInt(str: String, ints: Int*): Unit = {
    println("str = " + str)
    var i = 0
    for (int <- ints) {
      println("ints value[" + i + "] = " + int)
      i = i + 1
    }
  }
}
