package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.zerocore

import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

object test04 {
  def main(args: Array[String]): Unit = {
    //    val spark = SparkSession
    //      .builder()
    //      .appName(s"${this.getClass.getSimpleName}")
    //      .master("local[4]")
    //      .getOrCreate()
    //    import spark.implicits._
    val conf = new SparkConf().setMaster("local[4]").setAppName(s"${this.getClass.getSimpleName}")
    val sc = new SparkContext(conf)

    val lines = sc.textFile("/Users/hiwes/data/test.txt")
    val enen: RDD[(String, String)] = lines.mapPartitions(ite => {
      ite.map(str => {
        val a1 = str.split(" ")(0)
        val a2 = str.split(" ")(1)
        (a1, a2)
      })
    })

    val result = enen.map(tup => {
      val inArr = tup._2.split(",").map(_.toDouble)
      val vec = Vectors.dense(inArr)
      (tup._1, vec)
    })

    val clusterModel = KMeans.train(result.map(_._2), 3, 5)

    result.foreach(tup => {
      val c = clusterModel.predict(tup._2)
      println(tup._1+"————————:"+s"${tup._2.toString}" + "————————属于: " + c)
    })
  }
}
