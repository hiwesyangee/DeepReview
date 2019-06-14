package com.hiwes.cores.sparksql

import org.apache.spark.Partitioner
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row}
import utils.SparkUtils

object Test {
  def main(args: Array[String]): Unit = {
    val spark = SparkUtils.getSparkSession()
    import spark.implicits._
    //    val sc = spark.sparkContext

    //分区数量
    val partitions: Int = 4

    val user: DataFrame = spark.read.text("/Users/hiwes/data/facetime/user.txt")
    val transaction: DataFrame = spark.read.text("/Users/hiwes/data/facetime/transaction.txt")

    // 1.实现toDF
    val userDf = user.map(row => {
      val end = row.getString(0)
      val arr = end.split(",")
      (arr(0), arr(1))
    }).toDF("id", "email")

    userDf.show(false)

    val transactionDf = transaction.map(row => {
      val end = row.getString(0)
      val arr = end.split(",")
      (arr(0), arr(1), arr(2))
    }).toDF("id", "transaction_type", "transaction_amount")

    transactionDf.show(false)

    val end: DataFrame = userDf.join(transactionDf, "id")
    end.show(false)

    end.sort("transaction_type","id").show(false)


//    val endRdd = end.rdd
//    val valueToKey: RDD[((String, Int), Int)] = endRdd
//      .map(row => {
//        ((row.getString(2) + "_" + row.getString(0), row.getString(3).toInt), row.getString(3).toInt)
//      }).sortBy(_._1)
//
//
//    implicit def tupleOrderingDesc = new Ordering[Tuple2[String, Int]] {
//      override def compare(x: Tuple2[String, Int], y: Tuple2[String, Int]): Int = {
//        if (y._1.compare(x._1) == 0) -y._2.compare(x._2)
//        else -y._1.compare(x._1)
//      }
//    }
//
//    val sorted: RDD[((String, Int), Int)] = valueToKey.repartitionAndSortWithinPartitions(new SortPartitioner(partitions))
//    val result = sorted.map {
//      case (k, v) => (k._1, v)
//    }.reduceByKey(_ + _)
//
//    result.foreachPartition(ite => {
//      ite.foreach(println)
//    })
  }


}

/**
  * 自定义排序分区
  **/
class SortPartitioner(partitions: Int) extends Partitioner {

  require(partitions > 0, s"分区的数量($partitions)必须大于零。")

  def numPartitions: Int = partitions

  def getPartition(key: Any): Int = key match {
    case (k: String, v: Int) => math.abs(k.hashCode % numPartitions)
    case null => 0
    case _ => math.abs(key.hashCode % numPartitions)
  }

  override def equals(other: Any): Boolean = other match {
    case o: SortPartitioner => o.numPartitions == numPartitions
    case _ => false
  }

  override def hashCode: Int = numPartitions
}
