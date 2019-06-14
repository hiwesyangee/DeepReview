package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.summary.pipelines

import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.sql.{Row, SparkSession}

/**
  * Pipeline之:Estimator Transformer Param估计变压器参数示例
  */
object EstimatorTransformerAndParamExample {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("EstimatorTransformerParamExample")
      .master("local[4]")
      .getOrCreate()

    // 从(标签，特性)元组列表中准备培训数据。
    val training = spark.createDataFrame(Seq(
      (1.0, Vectors.dense(0.0, 1.1, 0.1)),
      (0.0, Vectors.dense(2.0, 1.0, -1.0)),
      (0.0, Vectors.dense(2.0, 1.3, 1.0)),
      (1.0, Vectors.dense(0.0, 1.2, -0.5))
    )).toDF("label", "features")

    // 创建一个逻辑回归实例。这个实例是一个估计器。
    val lr = new LogisticRegression()
    // 打印出参数、文档和任何默认值。然后使用setter方法设置自己的参数
    println("LogisticRegression parameters:\n" + lr.explainParams() + "\n")
    lr.setMaxIter(10).setRegParam(0.01)

    // 学习逻辑回归模型。这将使用存储在lr中的参数。
    val model1 = lr.fit(training)
    // 因为model1是一个模型(即，由估计器产生的变压器)，
    // 我们可以查看fit()中使用的参数。
    // 这将打印参数(名称:值)对，其中名称是这个LogisticRegression实例的惟一id。
    println("Model 1 was fit using parameters: " + model1.parent.extractParamMap)

    // 我们可以使用ParamMap来指定参数，它支持几种指定参数的方法。
    val paramMap = ParamMap(lr.maxIter -> 20)
      .put(lr.maxIter, 30) // 指定1参数。这会覆盖原来的maxIter。
      .put(lr.regParam -> 0.1, lr.threshold -> 0.55) // 指定多个参数。

    // One can also combine ParamMaps.
    val paramMap2 = ParamMap(lr.probabilityCol -> "myProbability") // 更改输出列名。
    val paramMapCombined = paramMap ++ paramMap2

    // 现在使用parammapcombination参数的新模型。
    // paramMapCombined overrides all parameters set earlier via lr.set* methods.
    // 提前通过lr.set设置 paramMapCombined
    // 重写所有的参数。
    val model2 = lr.fit(training, paramMapCombined)
    println("Model 2 was fit using parameters: " + model2.parent.extractParamMap)

    // 准备测试数据。
    val test = spark.createDataFrame(Seq(
      (1.0, Vectors.dense(-1.0, 1.5, 1.3)),
      (0.0, Vectors.dense(3.0, 2.0, -0.1)),
      (1.0, Vectors.dense(0.0, 2.2, -1.5))
    )).toDF("label", "features")

    // 使用transform .transform()方法对测试数据进行预测。
    // LogisticRegression。transform只使用" features "列。
    // 注意，model2.transform()输出一个“myProbability”列，而不是通常的“probability”列，
    // 因为重新命名了lr。probabilityCol参数之前。
    model2.transform(test)
      .select("features", "label", "myProbability", "prediction")
      .collect()
      .foreach { case Row(features: Vector, label: Double, prob: Vector, prediction: Double) =>
        println(s"($features, $label) -> prob=$prob, prediction=$prediction")
      }

    spark.stop()
  }
}
