package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.zerocore

import org.apache.spark.SparkConf
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.{IsotonicRegression, LinearRegression}
import org.apache.spark.sql.SparkSession

import scala.util.Random

object test022 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(s"${this.getClass.getSimpleName}").setMaster("local[4]")
    val spark = SparkSession.builder().config(conf).getOrCreate()

    val file = spark.read.format("csv").option("sep", ";")
      .option("header", "true")
      .load("/Users/hiwes/Downloads/coding-271/ch7/linear/house.csv")
    import spark.implicits._

    val random = new Random()
    val data = file.select("square", "price").map(row => {
      (row.getAs[String](0).toDouble,
        row.getString(1).toDouble,
        random.nextDouble())
    })
      .toDF("square", "price", "rand")
      .sort("rand")

    val ass = new VectorAssembler().setInputCols(Array("square"))
      .setOutputCol("features")

    val dataset = ass.transform(data)

    val Array(training, test) = dataset.randomSplit(Array(0.8, 0.2))

    //    val lr = new LinearRegression().setStandardization(true).setMaxIter(10).setFeaturesCol("features")
    //      .setLabelCol("price")
    //    val model = lr.fit(training)
    //    model.transform(test).show(false)


//    val lr = new LogisticRegression().setMaxIter(10).setFeaturesCol("features").setLabelCol("price")
//      .setRegParam(0.3).setElasticNetParam(0.8)

    val ir = new IsotonicRegression().setFeaturesCol("features").setLabelCol("price")

    val model = ir.fit(training)
    model.transform(test).select("price", "prediction").show(100, false)
  }
}
