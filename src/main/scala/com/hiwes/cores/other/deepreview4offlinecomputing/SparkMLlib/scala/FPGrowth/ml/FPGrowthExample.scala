package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala.FPGrowth.ml

import org.apache.spark.ml.fpm.FPGrowth
import org.apache.spark.sql.{DataFrame, SparkSession}

object FPGrowthExample {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName(s"${this.getClass.getSimpleName}")
      .master("local[4]")
      .getOrCreate()
    import spark.implicits._

    val dataset: DataFrame = spark.createDataset(Seq(
      "1 2 5",
      "1 2 3 5",
      "1 2")
    ).map(t => t.split(" ")).toDF("items")

    // 利用参数，设置最小支持度和最小置信度
    val fpgrowth: FPGrowth = new FPGrowth().setItemsCol("items").setMinSupport(0.5).setMinConfidence(0.6)
    val model = fpgrowth.fit(dataset)

    // 显示频繁项集。
    model.freqItemsets.show(false)

    // 显示生成的关联规则。
    model.associationRules.show(false)

    // transform根据所有关联规则检查输入项，并将结果总结为预测
    model.transform(dataset).show(false)


    spark.stop()
  }

}
