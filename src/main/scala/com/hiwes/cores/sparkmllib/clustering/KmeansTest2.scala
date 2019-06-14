package com.hiwes.cores.sparkmllib.clustering

import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.feature.{HashingTF, IDF, Tokenizer}
import utils.SparkUtils

object KmeansTest2 {

  case class Record(id: String, companyname: String, direction: String, productinfo: String)

  def main(args: Array[String]): Unit = {
    val spark = SparkUtils.getSparkSession()
    import spark.implicits._

    val records = spark.read.textFile("/Users/hiwes/data/dataframetext")
      .map(x => {
        val data = x.split(",")
        Record(data(0), data(1), data(2), data(3))
      }).toDF().cache()

    val wordsData = new Tokenizer()
      .setInputCol("productinfo")
      .setOutputCol("productwords")
      .transform(records)

    val tfData = new HashingTF()
      .setNumFeatures(20)
      .setInputCol("productwords")
      .setOutputCol("productFeatures")
      .transform(wordsData)

    val idfModel = new IDF()
      .setInputCol("productFeatures")
      .setOutputCol("features")
      .fit(tfData)
    // idfModel.save(...)

    val idfData = idfModel.transform(tfData)
    val traingData = idfData.select("id", "companyname", "features")

    val kmeans = new KMeans()
      .setSeed(1L)
      .setK(10)
      .setMaxIter(5)
      // .setTol()
      .setInitMode("k-means||")
      // .setInitSteps()
      .setFeaturesCol("features")
      .setPredictionCol("prediction")

    val kmeansModel = kmeans.fit(traingData)
    // kmeansModel.save(...)
    val kmeansData = kmeansModel.transform(traingData).cache()

    traingData.show(false)
    kmeansData.show(false)

    spark.stop()
  }
}

