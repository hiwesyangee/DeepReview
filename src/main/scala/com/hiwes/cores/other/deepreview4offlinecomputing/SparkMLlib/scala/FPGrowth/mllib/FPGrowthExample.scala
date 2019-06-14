package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala.FPGrowth.mllib

import com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala.AbstractParams
import scopt.OptionParser
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.fpm.FPGrowth

object FPGrowthExample {

  case class Params(
                     input: String = null,
                     minSupport: Double = 0.3,
                     numPartition: Int = -1) extends AbstractParams[Params]

  def main(args: Array[String]) {
    val defaultParams = Params()

    val parser = new OptionParser[Params]("FPGrowthExample") {
      head("FPGrowth: an example FP-growth app.")
      opt[Double]("minSupport")
        .text(s"minimal support level, default: ${defaultParams.minSupport}")
        .action((x, c) => c.copy(minSupport = x))
      opt[Int]("numPartition")
        .text(s"number of partition, default: ${defaultParams.numPartition}")
        .action((x, c) => c.copy(numPartition = x))
      arg[String]("<input>")
        .text("input paths to input data set, whose file format is that each line " +
          "contains a transaction with each item in String and separated by a space")
        .required()
        .action((x, c) => c.copy(input = x))
    }

    parser.parse(args, defaultParams) match {
      case Some(params) => run(params)
      case _ => sys.exit(1)
    }
  }

  def run(params: Params): Unit = {
    val conf = new SparkConf().setAppName(s"FPGrowthExample with $params").setMaster("local[4]")
    val sc = new SparkContext(conf)
    val transactions = sc.textFile(params.input).map(_.split(" ")).cache()

    println(s"Number of transactions: ${transactions.count()}")

    val model = new FPGrowth()
      .setMinSupport(params.minSupport) // 最小支持度
      .setNumPartitions(params.numPartition) // 分区数
      .run(transactions)

    println(s"Number of frequent itemsets: ${model.freqItemsets.count()}")

    // 打印频繁项集
    model.freqItemsets.collect().foreach { itemset =>
      println(itemset.items.mkString("[", ",", "]") + ", " + itemset.freq)
    }

    sc.stop()
  }

}
