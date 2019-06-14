package com.hiwes.cores.sparkcore

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Value类型的Transformation算子
  */
object SparkCore02 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]")
      .set("spark.default.paralleism", "10")
      .set("spark.driver.memory", "2g")
      .setAppName("Test")
    //    conf.set("spark.serializer",org.apache.spark.serializer.KryoSerializer)
    //    conf.set("spark.sql.shuffle.partitions", "50")
    // we recommend 2-3 tasks per CPU core in your cluster.我们建议在集群中每个CPU内核执行2-3个任务。
    val sc = new SparkContext(conf)

    val rdd = sc.parallelize(List(1, 2, 3, 4, 5))
    val mapRdd: RDD[Int] = rdd.map(x => x + 2)

    val rdd2 = sc.parallelize(List("i need work hard!", "work makes me happy."))
    val flatMapRdd: RDD[String] = rdd2.flatMap(_.split(" "))

    val mapPartitionsRdd: RDD[Array[String]] = rdd2.mapPartitions(ite => {
      ite.map(x => {
        x.split(" ")
      })
    })

    val glomRdd: RDD[Array[Int]] = rdd.glom()

    val rdd3: RDD[Int] = sc.parallelize(Seq(1, 2, 3, 4, 5, 6))
    val unionRdd = rdd.union(rdd3)
    val unionRdd2 = rdd ++ rdd3

    val rdd4 = sc.parallelize(List("a", "b", "c", "a"))
    val reduceByKeyRdd: RDD[(String, Iterable[Int])] = rdd4.map(x => (x, 1)).groupByKey(1)

    val rdd5 = sc.parallelize(Seq(1, 2, 3))
    val rdd5_2 = sc.parallelize(Seq(2, 3, 4))
    //    rdd5.subtract(rdd5_2).foreach(println)

    val rdd6: RDD[Int] = sc.parallelize(Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
    //    rdd6.sample(false,0.5,1).foreach(println)

    val rdd7 = sc.parallelize(Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
    val list: Array[Int] = rdd7.takeSample(false, 5, 9)
    //    list.foreach(println)

    //    rdd.cache()
    //    rdd.persist(StorageLevel.MEMORY_ONLY)
    //    rdd.persist(StorageLevel.MEMORY_AND_DISK_SER_2)

    val rdd8 = rdd7.map(x => (x, 1))
    rdd8.mapValues(x => x + 1).foreach(println)

    val lookUp: Seq[Int] = rdd8.lookup(3)


  }
}
