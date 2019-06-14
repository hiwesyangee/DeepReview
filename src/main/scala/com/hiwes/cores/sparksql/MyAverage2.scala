package com.hiwes.cores.sparksql

import org.apache.spark.sql.{Encoder, Encoders}
import org.apache.spark.sql.expressions.Aggregator

/**
  * 实现自定义强类型聚合函数
  */

case class Employee(name: String, salary: Long)

case class Average(var sum: Long, var count: Long)

object MyAverage2 extends Aggregator[Employee, Average, Double] {
  def zero: Average = Average(0L, 0L)

  def reduce(buffer: Average, employee: Employee): Average = {
    buffer.sum += employee.salary
    buffer.count += 1
    buffer
  }

  def merge(b1: Average, b2: Average): Average = {
    b1.sum += b2.sum
    b1.count += b2.count
    b1
  }

  def finish(reduction: Average): Double = reduction.sum.toDouble / reduction.count

  def bufferEncoder: Encoder[Average] = Encoders.product

  def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}
