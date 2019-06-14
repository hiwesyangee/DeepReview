package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.zerocore

import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.Random

object test02 {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("linear").setMaster("local[4]")
    val sc = new SparkContext(conf)
    val spark = SparkSession.builder().config(conf).getOrCreate()

    val file = spark.read.format("csv")
      .option("sep", ";")
      .option("header", "true")
      .load("/Users/hiwes/Downloads/coding-271/ch7/linear/house.csv")
    import spark.implicits._

    //打乱顺序
    val rand = new Random()
    val data = file.select("square", "price").map(
      row => (row.getAs[String](0).toDouble, row.getString(1).toDouble, rand.nextDouble()))
      .toDF("square", "price", "rand").sort("rand") //强制类型转换过程

    val ass = new VectorAssembler().setInputCols(Array("square")).setOutputCol("features")
    val dataset = ass.transform(data) //特征包装

    val Array(train, test) = dataset.randomSplit(Array(0.8, 0.2))
    // 拆分成训练数据集和测试数据集
    // train.show()
    /*
    // val lr = new LinearRegression().setStandardization(true).setMaxIter(10)
       .setFeaturesCol("features")
       .setLabelCol("price")
    // 创建一个对象
       val model = lr.fit(train) //训练

       model.transform(test).show()
    */

    val lr = new LogisticRegression().setLabelCol("price").setFeaturesCol("features")
      .setRegParam(0.3).setElasticNetParam(0.8).setMaxIter(10)
    val model = lr.fit(train)
    model.transform(test).show()
    val s = model.summary.totalIterations
    println(s"iter: ${s}")
  }
}
