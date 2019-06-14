package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala.FPGrowth.mllib

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.fpm.{AssociationRules, FPGrowth, FPGrowthModel}
import org.apache.spark.rdd.RDD

object SimpleFPGrowth {
  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("SimpleFPGrowth").setMaster("local[4]")
    val sc = new SparkContext(conf)

    val data = sc.textFile("file:///Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/data/mllib/sample_fpgrowth.txt")

    val transactions: RDD[Array[String]] = data.map(s => s.trim.split(' '))

    val fpg: FPGrowth = new FPGrowth()
      .setMinSupport(0.2)
      .setNumPartitions(10)
    val model: FPGrowthModel[String] = fpg.run(transactions)


    // 打印 频繁项集
    model.freqItemsets.collect().foreach { itemset =>
      println(itemset.items.mkString("[", ",", "]") + ", " + itemset.freq)
    }

    val minConfidence = 0.8
    // 打印 关联规则，并设置最小置信度
    model.generateAssociationRules(minConfidence).collect().foreach { rule =>
      println(
        rule.antecedent.mkString("[", ",", "]")
          + " => " + rule.consequent.mkString("[", ",", "]")
          + ", " + rule.confidence)
    }

    sc.stop()
  }
}
