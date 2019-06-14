package com.hiwes.cores.sparkmllib.clustering

import org.apache.spark.ml.clustering.GaussianMixture
import utils.SparkUtils

/**
  * 高斯混合模型算法。
  */
object GMMTest {
  def main(args: Array[String]): Unit = {
    val spark = SparkUtils.getSparkSession()
    import spark.implicits._

    val dataset = spark.read.format("libsvm").load("/Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/data/mllib/sample_kmeans_data.txt")

    dataset.show(false)

    val gmm = new GaussianMixture().setK(2)

    val model = gmm.fit(dataset)

    for (i <- 0 until model.getK) {
      println(s"Gaussian $i:\nweight=${model.weights(i)}\n" +
        s"mu=${model.gaussians(i).mean}\nsigma=\n${model.gaussians(i).cov}\n")
    }

    spark.stop()
  }
}
