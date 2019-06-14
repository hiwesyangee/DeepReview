package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.zerocore

import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.SparkSession

import scala.util.Random

object test021 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[2]")
      .appName(s"${this.getClass.getSimpleName}")
      .getOrCreate()
    import spark.implicits._

    val file = spark.read.format("csv").option("sep", ";").option("header", "true")
      .load("/Users/hiwes/Downloads/coding-271/ch7/linear/house.csv")

    val random = new Random()
    val data = file.select("square", "price").map(row => {
      (row.getString(0).toDouble, row.getString(1).toDouble, random.nextDouble())
    }).toDF("square", "price", "random").sort("random")

    val ass = new VectorAssembler().setInputCols(Array("square")).setOutputCol("features")
    val dataset = ass.transform(data)

    val Array(training, test) = dataset.randomSplit(Array(0.8, 0.2))

    val ls = new LinearRegression().setMaxIter(10).setRegParam(0.3).setFeaturesCol("features").setLabelCol("price")

    val model = ls.fit(training)
    model.transform(test).select("price", "prediction").show(false)

  }

}
