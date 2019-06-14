package com.hiwes.cores.other.deepreview4offlinecomputing.Review.SentimentAnalysis

import org.apache.spark.ml.classification.{DecisionTreeClassifier, NaiveBayes}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{HashingTF, IDF}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

import scala.util.Random

object Main {
  def main(args: Array[String]): Unit = {
    // 标准套路1
    val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)
    val spark = SparkSession.builder().config(conf).getOrCreate()
    spark.sparkContext.setLogLevel("WARN") ///日志级别
    // 标准套路2
    import spark.implicits._
    val rand = new Random()
    val posPath = "/Users/hiwes/Downloads/coding-271/ch11/sentiment_analysis/pos.txt"
    val negPath = "/Users/hiwes/Downloads/coding-271/ch11/sentiment_analysis/neg.txt"
    // 提取所有单词，并进行正负面标注，得到的结果还需要加入double的随机数用来排序
    val neg = spark.read.textFile(negPath).map(
      line => {
        (line.split(" ").filter(!_.equals(" ")), 0, rand.nextDouble())
      }).toDF("words", "value", "random")
    val pos = spark.read.textFile(posPath).map(
      line => {
        (line.split(" ").filter(!_.equals(" ")), 1, rand.nextDouble())
      }).toDF("words", "value", "random") //思考：这里把inner function提出重用来如何操作

    val data = neg.union(pos).sort("random") //思考：为什么不用join
    //data.show(false)
    //println(neg.count(),data.count())//合并

    //文本特征抽取
    val hashingTf = new HashingTF()
      .setInputCol("words")
      .setOutputCol("hashing")
      .transform(data)
    val idfModel = new IDF()
      .setInputCol("hashing")
      .setOutputCol("tfidf")
      .fit(hashingTf)
    val transformedData = idfModel
      .transform(hashingTf)
    val Array(training, test) = transformedData
      .randomSplit(Array(0.7, 0.3))

    //根据抽取到的文本特征，使用分类器进行分类，这是一个二分类问题
    //分类器是可替换的
    val bayes = new NaiveBayes()
      .setFeaturesCol("tfidf") //X
      .setLabelCol("value") //y
      .fit(training)
    val result = bayes.transform(test) //交叉验证
    result.show(false)

    //对模型的准去率进行评估
    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("value")
      .setPredictionCol("prediction")
      .setMetricName("accuracy")
    val accuracy = evaluator.evaluate(result)
    println(s"""accuracy is $accuracy""")


    result.select("prediction").foreachPartition(ite =>{
      ite.foreach(row =>{
        val end = row.getString(0)  // 这就实现了将结果获取出来了噻
      })
    })

    //重构思考：
    //尝试用pipeline重构代码
    //尝试用模型预测随便属于一句话的情感，例如：
    //You are a bad girl,I hate you. ^_^
  }
}
