package com.hiwes.cores.other.deepreview4datastructures.Algorithm

/**
  * 使用scala实现冒泡排序算法.
  */
object BubbleSort {
  def main(args: Array[String]): Unit = {
    val list = List(3, 12, 43, 23, 7, 1, 2, 0)
    println(sort(list))
  }

  // 定义一个方法，传入的参数是要进行排序的List集合，输出的是排序后的List集合
  def sort(list: List[Int]): List[Int] = list match {
    case List() => List()
    case head :: tail => compute(head, sort(tail))
  }

  // 计算方法。
  def compute(data: Int, dataSet: List[Int]): List[Int] = dataSet match {
    case List() => List(data)
    case head :: tail => if (data <= head) data :: dataSet else head :: compute(data, tail)
  }

}
