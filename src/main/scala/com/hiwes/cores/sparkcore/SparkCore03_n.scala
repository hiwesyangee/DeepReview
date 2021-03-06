package com.hiwes.cores.sparkcore

/**
  * 使用scala实现冒泡排序和快速排序
  */
object SparkCore03_n {
  def main(args: Array[String]): Unit = {
    val list = List(1, 10, 8, 4, 2, 5, 7, 9, 3, 6)
    println(bubbleSort(list))
    println("===================")
    println(quickSort(list))
  }

  def bubbleSort(list: List[Int]): List[Int] = {
    list match {
      case List() => List()
      case head :: tail => compute(head, bubbleSort(tail))
    }
  }

  def compute(data: Int, dataSet: List[Int]): List[Int] = {
    dataSet match {
      case List() => List(data)
      case head :: tail => {
        if (data <= head) {
          data :: dataSet
        } else {
          head :: compute(data, tail)
        }
      }
    }
  }


  def quickSort(list: List[Int]): List[Int] = {
    list match {
      case Nil => Nil
      case List() => List()
      case head :: tail => {
        val (left, right) = tail.partition(_ < head)
        quickSort(left) ::: head :: quickSort(right)
      }

    }
  }

}
