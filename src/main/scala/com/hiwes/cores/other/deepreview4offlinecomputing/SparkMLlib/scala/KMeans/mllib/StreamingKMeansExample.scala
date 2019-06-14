package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala.KMeans.mllib

import org.apache.spark.SparkConf
import org.apache.spark.mllib.clustering.StreamingKMeans
import org.apache.spark.mllib.linalg
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamingKMeansExample {
  def main(args: Array[String]) {
//    if (args.length != 5) {
//      System.err.println(
//        "Usage: StreamingKMeansExample " +
//          "<trainingDir> <testDir> <batchDuration> <numClusters> <numDimensions>")
//      System.exit(1)
//    }
    val conf = new SparkConf().setAppName("StreamingKMeansExample")
    val ssc = new StreamingContext(conf, Seconds(10))

    val trainingDir: String = "/Users/hiwes/data/streamingData/kmeansData/training"
    val testDir: String = "/Users/hiwes/data/streamingData/kmeansData/test"
    val trainingData: DStream[linalg.Vector] = ssc.textFileStream(trainingDir).map(Vectors.parse)
    val testData: DStream[LabeledPoint] = ssc.textFileStream(testDir).map(LabeledPoint.parse)

    val model = new StreamingKMeans()
      .setK(4)
      .setDecayFactor(1.0)
      .setRandomCenters(2, 0.0)

    model.trainOn(trainingData)
    model.predictOnValues(testData.map(lp => (lp.label, lp.features))).print()

    ssc.start()
    ssc.awaitTermination()

  }

}
