package com.hiwes.cores.other.deepreview4offlinecomputing.Review

import java.util.Random

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.SparkConf
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.LinearRegression

/**
  * 测试MLlib回归算法
  */
object Regression {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("RegressionTest")
    val spark = SparkSession.builder().config(conf).getOrCreate()

    val path = "/Users/hiwes/Downloads/coding-271/ch7/linear/house.csv"
    val file: DataFrame = spark.read.format("csv")
      .option("sep", ";") // ;作为分隔符
      .option("header", "true") // 保留表头
      .load(path)
//    file.show(false)

    import spark.implicits._

    val random = new Random()
    val data = file.select("square", "price") // 提取面积和价格
      .map(row => (row.getAs[String](0).toDouble,
      row.getString(1).toDouble,
      random.nextDouble())  // 转换为Double格式，并且插入随机数，从而达到排序的效果
    ).toDF("square", "price", "random").sort("random")

    val ass = new VectorAssembler().setInputCols(Array("square")).setOutputCol("features")
    val dataset = ass.transform(data)

    val Array(training, test) = dataset.randomSplit(Array(0.8, 0.2)) // 比例切割

    val lr = new LinearRegression().setStandardization(true)
      .setMaxIter(10)
      .setFeaturesCol("features").setLabelCol("price")
    val model = lr.fit(training)
    model.transform(test).show(false)
  }

}
