package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.zerocore

import org.apache.spark.SparkConf
import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.SparkSession

import scala.util.Random

object Main {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("gender").setMaster("local[2]")
    val session = SparkSession.builder().config(conf).getOrCreate()
    val sc = session.sparkContext

    // \\[(.*?)\\]
    // .表示任意字符，*?表示重复任意次,但尽可能少重复，[]正则表达式中关键符号,需要\进行转义
    val pattern = (filename: String, category: Int) => {
      val patternString = "\\[(.*?)\\]".r // .r表示正则表达式是scala写法
      val rand = new Random()
      sc.textFile(filename)
        .flatMap(text => patternString.findAllIn(text.replace(" ", "")))
        .map(text => {
          val pairwise = text.substring(1, text.length - 1).split(",")
          (pairwise(0).toDouble, pairwise(1).toDouble, category, rand.nextDouble())
        })
    }
    val male = pattern("male.txt", 1)
    val female = pattern("female.txt", 2)

    val maleDF = session
      .createDataFrame(male)
      .toDF("height", "weight", "category", "rand")
    val femaleDF = session
      .createDataFrame(female)
      .toDF("height", "weight", "category", "rand")
    val dataset = maleDF.union(femaleDF).sort("rand")
    val assembler = new VectorAssembler()
      .setInputCols(Array("height", "weight"))
      .setOutputCol("features")

    val transformedDataset = assembler.transform(dataset)
    transformedDataset.show()
    val Array(train, test) = transformedDataset.randomSplit(Array(0.8, 0.2))

    val classifier = new DecisionTreeClassifier()
      .setFeaturesCol("features")
      .setLabelCol("category")
    val model = classifier.fit(train)
    val result = model.transform(test)
    result.show()

    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("category")
      .setPredictionCol("prediction")
      .setMetricName("accuracy")
    val accuracy = evaluator.evaluate(result)
    println(s"""accuracy is $accuracy""")
  }
}
