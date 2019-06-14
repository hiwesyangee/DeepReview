package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala.FPGrowth.ml

import org.apache.spark.ml.fpm.{FPGrowth, FPGrowthModel}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

object OwnFPGroethExample {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[4]")
      .appName(s"${this.getClass.getSimpleName}")
      .getOrCreate()

    import spark.implicits._

    val dataset = spark.createDataset(Seq(
      "1 2 5",
      "2 4",
      "2 3",
      "1 2 4",
      "1 3",
      "2 3",
      "1 3",
      "1 2 3 5",
      "1 2 3"
    )).map(x => x.split(" ")).toDF("items")

    val fpGrowth = new FPGrowth().setItemsCol("items").setMinSupport(0.2).setMinConfidence(0.6)
    val model: FPGrowthModel = fpGrowth.fit(dataset)

    model.freqItemsets.show(false)
    model.associationRules.show(false)
    model.transform(dataset).show(false)


    spark.stop()

  }

}
