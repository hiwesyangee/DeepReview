package com.hiwes.cores.other.deepreview4offlinecomputing.SparkCore.Scala

import org.apache.spark.ml.clustering.{KMeans, KMeansModel}
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName(s"${this.getClass.getSimpleName}")
      .master("local[2]")
      .getOrCreate()
    val sc = spark.sparkContext

    //    val rdd: RDD[Int] = spark.sparkContext.parallelize(Seq(11, 2, 3, 4))
    //    val en: Int = rdd.max()
    //    println(en)

    val data: RDD[Array[Double]] = sc.parallelize(Seq(
      Array(1.0, 2.0, 1.0, 1.0, 4.0),
      Array(1.0, 1.0, 4.0, 4.0, 5.0),
      Array(1.0, 2.0, 3.0, 2.0, 5.0)
    ))

    import spark.implicits._
    val df: DataFrame = data.toDF("features")

    val kmeans = new KMeans().setK(2).setMaxIter(5)
    val model: KMeansModel = kmeans.fit(df)

    val data2 = sc.parallelize(Array(2.0, 3.0, 5.0, 5.0, 4.0))
    val df2 = data2.toDF("features")
    val enen: DataFrame = model.transform(df2)
    enen.show(false)

    sc.stop()
    spark.stop()

  }
}
