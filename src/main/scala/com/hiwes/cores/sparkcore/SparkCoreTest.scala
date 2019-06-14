package com.hiwes.cores.sparkcore

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

object SparkCoreTest {
  def main(args: Array[String]): Unit = {
    //    val conf = new SparkConf().setAppName("test").setMaster("local[2]")
    //    val sc = new SparkContext(conf)
    //
    //    // 使用range创建
    //    //    val rdd: RDD[Long] = sc.range(1,10,1)
    //    //    rdd.foreach(println)
    //
    //    // 使用一个本地的scala集合创建RDD
    //    val seq = Seq(1, 2, 3, 4, 5)
    //    val rdd = sc.makeRDD(seq)
    //    rdd.foreach(println)

    //    val spark = SparkSession.builder().master("local[2]").appName("test").getOrCreate()
    //
    //    import spark.implicits._
    //
    //    val df = spark.read.text("/Users/hiwes/Downloads/sqlResult_2400046.txt")
    //
    //    df.createOrReplaceTempView("history")
    //
    //    val end: DataFrame = df.sqlContext.sql("select * from history")
    //
    //    end.show(false)

    // test1()
    // test2()
    // test3()
    test4()
  }

  // case class 必须放在方法作用域之外。
  case class Person(name: String, age: Long)

  /** 使用case class创建RDD */
  def test1(): Unit = {
    val spark = SparkSession.builder().appName("master").master("local[2]").getOrCreate()
    // /Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources

    // 必要操作，添加隐式转换
    import spark.implicits._
    val peopleRDD = spark.sparkContext.textFile("/Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/people.txt")
      .map(_.split(",")).map(attributes => Person(attributes(0), attributes(1).trim.toInt))

    val peopleDF = peopleRDD.toDF

    peopleDF.createOrReplaceTempView("people")

    val teenagersDF = spark.sql("select name,age from people where age between 13 and 19")
    teenagersDF.map(teenager => "Name: " + teenager(0)).show(false)

    // 没有用于数据集的预定义编码器。
    implicit val mapEncoder = org.apache.spark.sql.Encoders.kryo[Map[String, Any]]
    // 原始类型和case类也可以定义为:
    // implicit val stringIntMapEncoder: Encoder[Map[String, Any]] = ExpressionEncoder()

    val arr: Array[Map[String, Any]] = teenagersDF.map(teenager => teenager.getValuesMap[Any](List("name", "age"))).collect()

    for (a <- arr) {
      println(a)
    }

  }

  /** 使用Schema创建RDD */
  def test2(): Unit = {
    val spark = SparkSession.builder().appName("masterr").master("local[2]").getOrCreate()

    val peopleRDD = spark.sparkContext.textFile("/Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/people.txt")

    // 使用String来模拟列名schema
    val schemaString = "name age"

    // 通过String创建String数组，然后转换为字段数组————————固定写法。
    val fields: Array[StructField] = schemaString.split(" ").map(fieldName => StructField(fieldName, StringType, true))

    // 将Array转换为Schema的类型:StructType
    val schema = StructType(fields)

    // 将RDD[String]转换为RDD[Row]————————trim：
    val rowRDD: RDD[Row] = peopleRDD.map(_.split(",")).map(attributes => Row(attributes(0), attributes(1).trim))

    // 通过RDD[Row]和Schema创建DAtaFrame
    val peopleDF = spark.createDataFrame(rowRDD, schema)

    peopleDF.createOrReplaceTempView("people")

    val results = spark.sql("SELECT name FROM people")

    //    results.map(attributes => "Name: " + attributes(0)).show(false)
  }

//  def test3(): Unit = {
  //    //    val spark = SparkSession.builder().appName("masterr").master("local[2]").getOrCreate()
  //    val conf = new SparkConf().setMaster("local[2]").setAppName("master")
  //    val sc = new SparkContext(conf)
  //
  //    val line = sc.textFile("/Users/hiwes/data/test.txt")
  //
  //    val need: RDD[String] = line.filter(line => line.replaceAll("\"", "").split(",").length == 11)
  //
  //    need.filter(line => {
  //      val arr = line.replaceAll("\"", "").split(",")
  //      XianChiFanLimitUtils.workIdIsPlanner(arr(2))
  //    }).foreach(println)
  //
  //
  //  }

  case class Tag(name: String, label: Int)

  def test4(): Unit = {
    val spark = SparkSession.builder().master("local[2]").appName("master").getOrCreate()
    import spark.implicits._

    val lineRDD = spark.sparkContext.textFile("/Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/people.txt")

    val dataframe1 = lineRDD.map(_.split(",")).map(arr => Tag(arr(0), arr(1).trim.toInt)).toDF

    dataframe1.show(false)

    val schemaStr = "name,label" // 模拟schema内容
    val schemaArr = schemaStr.split(",").map(arr => StructField(arr, StringType, true)) // 数组转换
    val schema = StructType(schemaArr)  // StructType转换

    val rowRDD = lineRDD.map(_.split(",")).map(arr => Row(arr(0), arr(1).trim)) // RDD类型转换
    val dataframe2 = spark.createDataFrame(rowRDD, schema)

    dataframe2.show(false)
  }


  def test4_2(): Unit ={
    val spark = SparkSession.builder().master("local[2]").appName("test").getOrCreate()
    import spark.implicits._

    val rowRDD = spark.sparkContext.textFile("/Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/people.txt")
      .map(_.split(",")).map(arr => Row(arr(0),arr(1).trim))

    val schemaStr = "name,age"
    val schemaArr = schemaStr.split(",").map(arr => StructField(arr,StringType,true))
    val schema: StructType = StructType(schemaArr)

    val df = spark.createDataFrame(rowRDD,schema)

    df.show(false)
  }


}
