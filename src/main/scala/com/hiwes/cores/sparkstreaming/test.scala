package com.hiwes.cores.sparkstreaming

import com.hiwes.cores.JavaHBaseUtils

object test {
  def main(args: Array[String]): Unit = {
    val end = JavaHBaseUtils.getValue("planner_info","1c006333-d761-4c4a-b26c-6d48f6598f17","info","name")
    println(end)
  }

}
