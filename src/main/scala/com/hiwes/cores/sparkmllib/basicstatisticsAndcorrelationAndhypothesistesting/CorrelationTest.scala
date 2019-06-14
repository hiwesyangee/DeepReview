package com.hiwes.cores.sparkmllib.basicstatisticsAndcorrelationAndhypothesistesting

import org.apache.spark.ml.linalg
import org.apache.spark.ml.linalg.{Matrix, Vectors}
import org.apache.spark.ml.stat.Correlation
import org.apache.spark.sql.{DataFrame, Row}
import utils.SparkUtils

/**
  * 相关性算法Correlation.
  * 封装于org.apache.spark.mllib.stat._中
  */
object CorrelationTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkUtils.getSparkSession()
    import spark.implicits._

    // 使用Vectors工厂类生成对象，dense密集向量，sparse稀疏向量
    //    val data: Seq[linalg.Vector] = Seq(
    //      Vectors.sparse(4, Seq((0, 1.0), (3, -2.0))),
    //      Vectors.dense(4.0, 5.0, 0.0, 3.0),
    //      Vectors.dense(6.0, 7.0, 0.0, 8.0),
    //      Vectors.sparse(4, Seq((0, 9.0), (3, 1.0)))
    //    )

    val data: Seq[linalg.Vector] = Seq(
      Vectors.dense(4.0, 5.0, 1.0, 3.0),
      Vectors.dense(4.0, 5.0, 0.0, 3.0),
      Vectors.dense(6.0, 7.0, 0.0, 8.0),
      Vectors.dense(6.0, 5.0, 1.0, 8.0)
    )

    val df: DataFrame = data.map(Tuple1.apply).toDF("features")
    val Row(coeff1: Matrix) = Correlation.corr(df, "features").head
    println("Pearson correlation matrix——皮尔森相关矩阵:\n" + coeff1.toString) // 默认皮尔森。

    val Row(coeff2: Matrix) = Correlation.corr(df, "features", "spearman").head
    println("Spearman correlation matrix——斯皮尔曼相关矩阵:\n" + coeff2.toString)

    spark.stop()

  }
}
