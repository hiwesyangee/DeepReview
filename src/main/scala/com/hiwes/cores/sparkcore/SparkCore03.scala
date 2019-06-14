package com.hiwes.cores.sparkcore

/**
  * 利用Scala实现排序
  */
object SparkCore03 {
  def main(args: Array[String]): Unit = {
    val list = List(3, 12, 43, 23, 7, 1, 2, 0)
    println(bubbleSort(list))
    println("===============")
    println(quickSort(list))
    println("===============")
    println(mergedSort((x: Int, y: Int) => x < y)(list))
  }


  /**
    * 冒泡排序
    *
    * 如果空list直接返回；
    * 如果包含head和tail的list，会调用递归函数compute；
    * compute内部首先排除只有一个元素的情况；
    * 如果有多个元素，并且head >= tail中list的头元素data，则返回
    */

  def bubbleSort(list: List[Int]): List[Int] = {
    list match {
      case List() => List()
      case head :: tail => compute(head, bubbleSort(tail))
    }
  }

  def compute(data: Int, dataSet: List[Int]): List[Int] = {
    dataSet match {
      case List() => List(data)
      case head :: tail => if (data <= head) data :: dataSet else head :: compute(data, tail)
    }
  }

  // 快速排序
  def quickSort(list: List[Int]): List[Int] = {
    list match {
      case Nil => Nil
      case List() => List()
      case head :: tail =>
        val (left, right) = tail.partition(_ < head)
        quickSort(left) ::: head :: quickSort(right)
    }
  }

  // 归并排序
  def mergedSort[T](less: (T, T) => Boolean)(list: List[T]): List[T] = {
    def merged(xList: List[T], yList: List[T]): List[T] = {
      (xList, yList) match {
        case (Nil, _) => yList
        case (_, Nil) => xList
        case (x :: xTail, y :: yTail) => {
          if (less(x, y)) x :: merged(xTail, yList)
          else
            y :: merged(xList, yTail)
        }
      }
    }

    val n = list.length / 2
    if (n == 0) list
    else {
      val (x, y) = list splitAt n
      merged(mergedSort(less)(x), mergedSort(less)(y))
    }
  }

}
