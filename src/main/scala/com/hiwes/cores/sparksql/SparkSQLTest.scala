package com.hiwes.cores.sparksql

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.DateType
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.storage.StorageLevel

object SparkSQLTest {
  def main(args: Array[String]): Unit = {
    // test3()
    // test4()
    // test5()
    // test6()
    // test7()
    test8()
  }


  def test1(): Unit = {
    val spark = SparkSession.builder().appName("sparksql").master("local[2]").getOrCreate()

    spark.udf.register("MyAverage", MyAverage)

    val df = spark.read.json("/Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/employees.json")
    df.createOrReplaceTempView("employees")

    df.show(false)

    val result = spark.sql("SELECT myAverage(salary) as average_salary FROM employees")
    result.show(false)
  }


  def test2(): Unit = {
    val spark = SparkSession.builder().appName("sparksql").master("local[2]").getOrCreate()

    import spark.implicits._

    val ds = spark.read.json("/Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/employees.json").as[Employee]
    ds.show()

    val averageSalary = MyAverage2.toColumn.name("average_salary")
    val result = ds.select(averageSalary)
    result.show()
  }

  def test3(): Unit = {
    val spark = SparkSession.builder().appName("sparksql").master("local[2]").getOrCreate()

    import spark.implicits._

    val userDf = spark.read.load("/Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/users.parquet")
    userDf.show(false)

    userDf.select("name", "favorite_color").write.save("/Users/hiwes/data/namesAndFavColors.parquet")

  }

  def test4(): Unit = {
    val spark = SparkSession.builder().appName("sparksql").master("local[2]").getOrCreate()

    import spark.implicits._

    val peopleDF = spark.read.format("json").load("/Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/people.json")
    peopleDF.select("name", "age").write.format("parquet").save("/Users/hiwes/data/namesAndAges.parquet")

    val peopleDFCsv = spark.read.format("csv")
      .option("sep", ";")
      .option("inferSchema", "true")
      .option("header", "true")
      .load("/Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/people.csv")

    peopleDFCsv.select("name", "age").write.format("parquet").save("/Users/hiwes/data/namesAndAges2.parquet")

  }


  def test5(): Unit = {
    val spark = SparkSession.builder().appName("sparksql").master("local[2]").getOrCreate()
    import spark.implicits._

    //    val df = spark.sql("select name,favorite_color from parquet.`/Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/users.parquet`")
    //    df.show(false)

    val df = spark.sql("select * from csv.`/Users/hiwes/Downloads/sqlResult_2411272.csv`")
    df.show(false)
  }

  def test6(): Unit = {
    val spark = SparkSession.builder().appName("sparksql").master("local[2]")
      .config("spark.sql.hive.convertMetastoreParquet", true) // 设置为false的时候，Spark将使用Hive SerDe作为持久表支持。
      .getOrCreate()
    import spark.implicits._

    //    val df = spark.sql("select name,favorite_color from parquet.`/Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/examples/src/main/resources/users.parquet`")
    //    df.show(false)

    //    df.write.mode("").save()

    //    df.write.bucketBy(42, "name").sortBy("name").saveAsTable("tablename")

    //    df.write.partitionBy("name").format("parquet").save("parquetPath")

    val otherPeopleDataset = spark.createDataset(
      """{"name":"Yin","address":{"city":"Columbus","state":"Ohio"}}""" :: Nil)
    val otherPeople = spark.read.json(otherPeopleDataset)
    otherPeople.show(false)
  }

  case class Data(id: Int, name: String, phone: Int, label: String, utime: String)

  def test7(): Unit = {
    val spark = SparkSession.builder().appName("sparksql").master("local[2]")
      //      .config("spark.sql.hive.convertMetastoreParquet", true) // 设置为false的时候，Spark将使用Hive SerDe作为持久表支持。
      .getOrCreate()
    import spark.implicits._

    //    val df: DataFrame = spark.read.format("jdbc")
    //      .option("url", "jdbc:mysql://hiwes:3306/test")
    //      .option("driver", "com.mysql.jdbc.Driver")
    //      .option("dbtable", "testTable")
    //      .option("user", "root")
    //      .option("password", "root").load()
    //
    //    df.show(false)
    //
    //    df.createOrReplaceTempView("testTable0")

    val str = "1,测试,1,测试,2019-05-29 16:21:46"

    val df: DataFrame = spark.sparkContext.parallelize(Seq(str.split(","))).map(arr => Data(arr(0).toInt, arr(1), arr(2).toInt, arr(3), arr(4))).toDF()

    df.persist(StorageLevel.MEMORY_ONLY_SER)

    df.write.format("jdbc")
      .option("url", "jdbc:mysql://hiwes:3306/test")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("dbtable", "testTable")
      .option("user", "root")
      .option("password", "root").mode("append").save()

    df.unpersist()
  }

  case class Sex(name: String, sex: String)

  def test8(): Unit = {
    val spark = SparkSession.builder().appName("sparksql").master("local[2]")
      //      .config("spark.sql.hive.convertMetastoreParquet", true) // 设置为false的时候，Spark将使用Hive SerDe作为持久表支持。
      .getOrCreate()
    import spark.implicits._

    //    val df: DataFrame = spark.read.format("jdbc")
    //      .option("url", "jdbc:mysql://hiwes:3306/test")
    //      .option("driver", "com.mysql.jdbc.Driver")
    //      .option("dbtable", "testTable")
    //      .option("user", "root")
    //      .option("password", "root").load()
    //
    //    df.show(false)
    //
    //    df.createOrReplaceTempView("testTable0")

    val str = "1,张三,1,宅男,2019-05-29 16:21:46"
    val df: DataFrame = spark.sparkContext.parallelize(Seq(str.split(","))).map(arr => Data(arr(0).toInt, arr(1), arr(2).toInt, arr(3), arr(4))).toDF()
    df.show(false)
    df.createOrReplaceTempView("data")

    val str2 = "张三,男"
    val df2: DataFrame = spark.sparkContext.parallelize(Seq(str2.split(","))).map(arr => Sex(arr(0), arr(1))).toDF()
    df2.show(false)
    df2.createOrReplaceTempView("sex")

    // 广播变量
    import org.apache.spark.sql.functions.broadcast
    broadcast(spark.table("sex")).join(spark.table("data"), "name").show(false)
    //    spark.table("sex").join(spark.table("data"),"name").show(false)

    df.rdd.map(row2str(_)).foreach(println)

  }

  def row2str(row: Row): String = {
    var end = ""
    for (i <- 0 to row.size - 1) {
      end += row(i) + ","
    }
    end.substring(0, end.length - 1)
  }
}
