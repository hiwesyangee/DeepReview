package com.yangee.cores

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
  * 实例1:
  * Estimator、Transformer、and Param。
  */
object Test1 {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.apache.hbase").setLevel(Level.WARN)
    Logger.getLogger("org.apache.hadoop").setLevel(Level.WARN)
    Logger.getLogger("org.apache.zookeeper").setLevel(Level.WARN)

    val spark = SparkSession.builder().appName("Test").master("local[2]").getOrCreate()
    // 1.准备带标签和特征的数据
    val training = spark.createDataFrame(Seq(
      (1.0, Vectors.dense(0.0, 1.1, 0.1)),
      (0.0, Vectors.dense(2.0, 1.0, -1.0)),
      (0.0, Vectors.dense(2.0, 1.3, 1.0)),
      (1.0, Vectors.dense(0.0, 1.2, -0.5))
    )).toDF("label", "features")

    // 2.创建逻辑回归的评估器
    val lr = new LogisticRegression()

    // 3.使用setter()方法设置参数
    lr.setMaxIter(10).setRegParam(0.01)

    // 4.使用存储在lr中的参数来训练一个模型
    val model1 = lr.fit(training)
    // 查看参数
    println(model1.parent.extractParamMap)

    // 5.使用paramMap选择指定的参数
    val paramMap = ParamMap(lr.maxIter -> 20).put(lr.maxIter, 30).put(lr.regParam -> 0.1, lr.threshold -> 0.55)
    val paramMap2 = ParamMap(lr.probabilityCol -> "myProbability")
    val paramMapCombined = paramMap ++ paramMap2

    val model2 = lr.fit(training, paramMapCombined)
    // 查看参数
    println(model2.parent.extractParamMap)

    // 6.准备测试数据
    val test = spark.createDataFrame(Seq(
      (1.0, Vectors.dense(-1.0, 1.5, 1.3)),
      (0.0, Vectors.dense(3.0, 2.0, -0.1)),
      (1.0, Vectors.dense(0.0, 2.2, -1.5))
    )).toDF("label", "features")

    // 7.预测结果
    val end1: DataFrame = model1.transform(test)
    val end2: DataFrame = model2.transform(test)

    model1.transform(test).select("label", "features", "probability", "prediction").collect()

    end1.show(false)
    end2.show(false)

    end2.foreachPartition(ite => {
      ite.foreach((ele: Row) => {
        println(ele.getAs("label").toString + "\t" + ele.getAs("prediction").toString)
      })
    })
  }
}
