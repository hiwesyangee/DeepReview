package com.hiwes.cores.sparkcore

object SparkCore03_3 {
  def main(args: Array[String]): Unit = {
    val list = List(1, 4, 7, 12, 5, 8, 2, 9, 6)
    println(quicksort(list))
  }

  //  def sort(list: List[Int]): List[Int] = {
  //    list match {
  //      case Nil => Nil // 空的List
  //      case List() => List()
  //      case head :: tail =>
  //        val (left, right) = tail.partition(_ < head)
  //        sort(left) ::: head :: sort(right) // :::连接两个list，::cons构造，向头部追加数据
  //    }
  //  }

  def quicksort(list: List[Int]): List[Int] = {
    list match {
      case Nil => Nil
      case List() => List()
      case head :: tail =>
        val (left, right) = tail.partition(_ < head)
        quicksort(left) ::: head :: quicksort(right)
    }
  }
}
