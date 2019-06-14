package com.yangee.cores

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.{HashingTF, Tokenizer}
import org.apache.spark.ml.{Pipeline, PipelineModel}

/**
  * 实例2:
  * Pipeline。
  */
object Test2 {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.apache.hbase").setLevel(Level.WARN)
    Logger.getLogger("org.apache.hadoop").setLevel(Level.WARN)
    Logger.getLogger("org.apache.zookeeper").setLevel(Level.WARN)

    val spark = SparkSession.builder().appName("Test").master("local[2]").getOrCreate()
    // 1.准备训练文档；
    val training = spark.createDataFrame(Seq(
      (0L, "a b c d e spark", 1.0),
      (1L, "b d", 0.0),
      (2L, "spark f g h", 1.0),
      (3L, "hadoop mapreduce", 0.0)
    )).toDF("id", "text", "label")

    // 2.配置ML管道，包含三个Stage: Tokenizer，HashingTF和LR
    val tokenizer = new Tokenizer()
      .setInputCol("text")
      .setOutputCol("words")
    val hashingTF = new HashingTF()
      // .setNumFeatures(100) //文本量很大的时候一定要指定特征数量
      .setInputCol(tokenizer.getOutputCol)
      .setOutputCol("features")
    val lr = new LogisticRegression().setMaxIter(10).setRegParam(0.01)

    val pipeline = new Pipeline().setStages(Array(tokenizer, hashingTF, lr))

    // 3.安装管道到数据上===>本质就是构建模型的过程
    val model: PipelineModel = pipeline.fit(training)

    // 4.保存管道到磁盘：包括安装好的model和未安装好的pipeline
//    pipeline.save("/Users/hiwes/data/pipeline/LogisticRegressionPipeline/")
//    model.save("/Users/hiwes/data/model/LogisticRegressionModel/")

    // 5.加载管道
//    val pipeline2 = Pipeline.load("/Users/hiwes/data/pipeline/LogisticRegressionPipeline/")
    val model2 = PipelineModel.load("/Users/hiwes/data/model/LogisticRegressionModel/")

    // 6.准备测试文档
    val test = spark.createDataFrame(Seq(
      (4L, "spark i j k"),
      (5L, "l m n"),
      (6L, "mapreduce spark"),
      (7L, "apache hadoop"),
      (8L, "spark f g h i")
    )).toDF("id", "text")

    // 7.预测结果
    model2.transform(test).show(false)
  }
}
