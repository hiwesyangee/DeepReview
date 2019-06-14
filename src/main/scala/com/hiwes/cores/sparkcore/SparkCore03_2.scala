package com.hiwes.cores.sparkcore

object SparkCore03_2 {
  def main(args: Array[String]): Unit = {
    val list = List(1, 6, 3, 22, 12, 5, 99, 31, 24)
    println(bubbleSort(list))
  }


  //  def sort(list: List[Int]): List[Int] = {
  //    list match {
  //      case List() => List() // 如果是空list，返回空
  //      case head :: tail => compute(head, sort(tail))
  //    }
  //  }
  //
  //  def compute(data: Int, dataSet: List[Int]): List[Int] = {
  //    dataSet match {
  //      case List() => List(data)
  //      case head :: tail => {
  //        if (data <= head) { // 如果外面的头小于等于里面的头
  //          data :: dataSet // 返回传进来的数据，相当于第一个元素不用变。
  //        } else { // 如果外面的更大，则返回的是里面的头和递归的（外面头::尾巴）
  //          head :: compute(data, tail) // 递归调用里面的头和递归外面的头和尾巴
  //        }
  //      }
  //    }
  //  }

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

}
