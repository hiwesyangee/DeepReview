package com.hiwes.cores.other.deepreview4offlinecomputing.SparkCore.Scala

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

/**
  * 深度复习之离线计算1:Spark Core（Scala版本）
  *
  * @author Hiwes
  * @since 2018/8/30 上午11:00
  * @version 1.0
  */

object SparkCoreTest {
  private val arr = Array(1, 2, 3, 4, 5)
  private val path: String = "/Users/hiwes/data/test.txt"
  private val hdfsPath: String = "hdfs://hiwes:8020/data/hdfsTest.txt"

  def main(args: Array[String]): Unit = {
    // 初始化Spark连接
    val conf = new SparkConf().setMaster("local[2]").setAppName("Test")
    val sc = new SparkContext(conf)

    // 初始化RDD1————————自创建数组方式:主要用于本地测试
    val data1: RDD[Int] = sc.parallelize(arr)
    //    val data0 = sc.parallelize(List(1, 2, 3, 4, 5))
    //    parallelize()方法默认的参数是一个Seq，但是依然可以接受数组作为参数传递
    // 初始化RDD2————————读取本地文件方式:主要用于本地临时性处理存储大量数据的文件
    val data2: RDD[String] = sc.textFile(path)
    // 初始化RDD3————————读取HDFS文件方式:最常用的生产环境文件，针对HDFS存储的大数据，进行离线批处理操作
    val data3: RDD[String] = sc.textFile(hdfsPath)
    // 初始化RDD4————————并增加分区数
    val data4: RDD[Int] = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 3)


    /**
      * Transformations(转换算子)——————map/flatMap/mapPartitions/mapPartitionsWithIndex
      */
    //    // map:对每个元素加一
    //    val transRdd1 = data1.map(x => x + 1)
    //    transRdd1.foreach(println)
    //    // flatMap:对每行元素进行分割
    //    val transRdd2 = data2.flatMap(_.split(","))
    //    transRdd2.foreach(x => {
    //      println(x)
    //    })
    // mapPartitions:
    //    data3.flatMap(_.split(",")).mapPartitions(ite => ite.map(x => x.toInt +1)).foreach(println)

    //    //    data4.mapPartitionsWithIndex(mapPartIndexFunc).foreach(println)
    //    data4.mapPartitionsWithIndex((i1, iter) => {  // 直接对分区号进行操作？
    //      var res = List[(Int, Int)]()
    //      while (iter.hasNext) {
    //        val next = iter.next()
    //        res = res.::(i1, next)
    //      }
    //      res.iterator
    //    }).foreach(println)
    /**
      * map函数会对每一条输入进行指定的操作，然后为每一条输入返回一个对象；
      * 而flatMap函数则是两个操作的集合—————————————————————正是“先映射后扁平化”：
      * 操作1：同map函数一样：对每一条输入进行指定的操作，然后为每一条输入返回一个对象
      * 操作2：最后将所有对象合并为一个对象
      * 最终flatMap返回的是一个迭代器。      常用来进行切分。
      * mapPartitions会对一个叠加器中每个元素施加新的函数，需要再对ite进行map操作。
      * mapPartitionsWithIndex和mapPartitions作用一样，但是需要加入分区索引。
      * 总结：能用mapPartitions的时候就用，避免大量创建对象，增加性能消耗。
      */

    /**
      * Transformations(转换算子)——————sample/takeSample
      */
    //    val transRdd1: RDD[Int] = data1.sample(true, 2)
    // withReplaceMent:元素是否能多次采样
    //         fraction:分数。样本的期望大小是RDD大小的一部分
    //                        *没有替换false:每个元素被选中的概率;分数必须为[0,1]
    //                        *替换true:选择每个元素的预期次数;分数必须大
    //                        *大于等于0
    //         seed:用于随机数生成器的种子,不好把控，一般设置为默认值
    //    transRdd1.foreach(println)
    //    val transRdd2: Array[Int] = data1.takeSample(false, 2) // 返回的是一个数组，并且会写入内存，仅适用于期望结果数组很小的时候。
    /**
      * 作为随机抽样的sample函数和takeSample函数，都有三个参数:
      * sample(withReplaceMent,fraction,seed)
      * withReplaceMent:元素是否能多次采样
      * fraction:分数。样本的期望大小是RDD大小的一部分
      * *没有替换:每个元素被选中的概率;分数必须为[0,1]
      * *替换:选择每个元素的预期次数;分数必须大
      * *大于等于0
      * seed:用于随机数生成器的种子,不好把控，一般设置为默认值
      * takeSample(withReplaceMent,num,seed)
      * withReplaceMent:元素是否能多次采样
      * num：返回的样本的大小
      * withReplacement=false;样本个数num大于父本个数时,只能返回父本个数
      * withReplacement=false;样本个数num小于父本个数时,返回样本个数
      * withReplacement=true;样本个数num大于父本个数时,返回样本个数
      * withReplacement=true;样本个数num小于父本个数时,返回样本个数
      * seed:用于随机数生成器的种子,不好把控，一般设置为默认值
      * 总结：一般使用sample函数进行随机抽样， takeSample会返回一个Array[T]数组，并且会写入内存，所以一般在期望返回数组很小的时候使用。
      */

    /**
      * Transformations(转换算子)——————union
      * union可以用++替代
      * 合并的两个RDD类型必须相同，合并后的元素未去重
      * 合并前的分区和合并后的分区相同，合并相同分区的元素
      */
    //    val data5 = data1.union(data4)
    //    val data6 = data1.map(x => (x, 1)).union(data4.map(x => (x, 2)))
    //    val data7 = data1.map(x => (x, 1)) ++ data4.map(x => (x, 2))
    //    val list: Array[Int] = data5.collect()
    //    for (i <- list) {
    //      println(i)
    //    }
    //
    //    println("————————————————————————-")
    //    data5.foreach(println)
    //    data6.foreach(println)
    //    val line1 = sc.parallelize(List(1, 2, 3, 4, 5))
    //    val line2 = sc.parallelize(List(1, 2, 3, 4, 5))
    //    line1.mapPartitionsWithIndex((i1, iter) => {
    //      var res = List[(Int, Int)]()
    //      while (iter.hasNext) {
    //        val next = iter.next()
    //        res = res.::(i1, next)
    //      }
    //      res.iterator
    //    }).foreach(println)
    //    println("______________________________")
    //    line2.mapPartitionsWithIndex((i1, iter) => {
    //      var res = List[(Int, Int)]()
    //      while (iter.hasNext) {
    //        val next = iter.next()
    //        res = res.::(i1, next)
    //      }
    //      res.iterator
    //    }).foreach(println)
    //    println("______________________________")
    //    (line1 ++ line2).mapPartitionsWithIndex((i1, iter) => {
    //      var res = List[(Int, Int)]()
    //      while (iter.hasNext) {
    //        val next = iter.next()
    //        res = res.::(i1, next)
    //      }
    //      res.iterator
    //    }).foreach(println)
    //    val line1: RDD[Int] = sc.parallelize(List(1,2,3,4))
    //    val line2: RDD[(Int, Int)] = line1.map(x => (x,1))
    //    val line3 = line2
    //    (line2 ++ line3).foreach(println)
    /**
      * union算子，可以使用++代替，要求两个RDD的类型一致，底层是直接将两个分区的数据进行拼接。
      * 总结：不管是否对RDD的数据进行分区：(分区/默认)，最终的元素是合并后的元素，分区不会合并，而是直接拼接。使用mapPartitionsWithIndex可以验证。
      */

    /**
      * Transformations(转换算子)——————intersection
      * 返回两个RDD的交集，并且去重。
      * def intersection(other: RDD[T], numPartitions: Int): RDD[T]
      * 参数numPartitions指定返回的RDD的分区数。
      * def intersection(other: RDD[T], partitioner: Partitioner)(implicit ord: Ordering[T] = null): RDD[T]
      * 参数partitioner用于指定分区函数：使用了隐式转换
      */
    //    val firstRdd = sc.parallelize(List(3, 4, 5, 6, 7))
    ////    data1.intersection(firstRdd, 1).foreach(println)
    //    // 在进行交际计算的时候，会直接进行去重。这个算子在内部执行了Shuffle。性能有所降低。
    //    val secondRdd = firstRdd.map(x => (x, 1))
    //    val endRdd = data1.map(x => (x, 1))
    //    secondRdd.intersection(endRdd,2).foreach(println)

    /**
      * intersection算子，求两个RDD的交集，并对内部元素进行去重。
      * this.map(v => (v, null)).cogroup(other.map(v => (v, null)), partitioner)
      * .filter { case (_, (leftGroup, rightGroup)) => leftGroup.nonEmpty && rightGroup.nonEmpty }
      * .keys
      * 总结：在内部执行了Shuffle，可以为生成的RDD直接指定分区数。
      */

    /**
      * Transformations(转换算子)——————distinct
      * 对一个RDD进行内部元素去重，可以设定返回RDD的分区数。
      */
    //    data1.union(sc.parallelize(List(1, 3, 5, 7, 9))).distinct(1).foreach(println)

    /**
      * distinct算子，求RDD的去重子集(也就是对自己去重)
      * ## 带分区数去重。
      * def distinct(numPartitions: Int)(implicit ord: Ordering[T] = null): RDD[T] = withScope {
      * map(x => (x, null)).reduceByKey((x, y) => x, numPartitions).map(_._1)
      * }
      * ## 不带分区数去重。
      * def distinct(): RDD[T] = withScope {
      * distinct(partitions.length)
      * }
      * 总结：在内部执行了Shuffle，可以为生成的RDD直接指定分区数。
      */

    /**
      * Transformations(转换算子)——————groupByKey/reduceByKey
      * 根据Key对Value进行聚合，并将函数func作用于Value。
      */
    //    val pair1 = data2.flatMap(_.split(",")).map(x => (x, 1))
    //    // 直接使用groupByKey对数据根据Key进行聚合，然后对Value迭代器直接调用sum()进行求和。
    //    val pair2 = pair1.groupByKey().map(x => (x._1, x._2.sum)) // 处理的结果中Value是一个迭代器，需要对迭代器进行操作
    //    // 使用reduceByKey对数据进行进行聚合，直接作用于相同的Key的Value。
    //    val pair3 = pair1.reduceByKey(_ + _) // 处理的结果直接就是要的结果，需要对Value进行操作。

    /**
      * groupByKey/reduceByKey。
      * 异同:
      * 相同处:
      * 都是对PairRDD的相同Key的Value进行聚合操作并运用函数func。
      * 不同处:
      * groupByKey对Value进行聚合，触发Shuffle，当调用 groupByKey时，所有的键值对(key-value pair)
      * 都会被移动,在网络上传输这些数据非常没必要，因此避免使用 GroupByKey。
      * 为了确定将数据对移到哪个主机，Spark会对数据对的key调用一个分区算法。
      * 当移动的数据量大于单台执行机器内存总量时Spark会把数据保存到磁盘上。
      * 不过在保存时每次会处理一个key的数据，所以当单个 key 的键值对超过内存容量会存在内存溢出的异常。
      * 这将会在之后发行的 Spark 版本中更加优雅地处理，这样的工作还可以继续完善。
      * 尽管如此，仍应避免将数据保存到磁盘上，这会严重影响性能。
      *
      * 当采用groupByKey时，由于它不接收函数，
      * spark只能先将所有的键值对(key-value pair)都移动，这样的后果是集群节点之间的开销很大，导致传输延时。
      *
      * 当采用reduceByKey时，Spark可以在每个分区移动数据之前将待输出数据与一个共用的key结合。
      * 然后lamdba函数在每个区上被再次调用来将所有值reduce成一个最终结果。
      *
      * 官网源码中都直接说了： If you are grouping in order to perform an
      * * aggregation (such as a sum or average) over each key, using `PairRDDFunctions.aggregateByKey`
      * * or `PairRDDFunctions.reduceByKey` will provide much better performance.
      * 如果你是要执行一个对每个Key进行聚合的操作(求和/平均值)，使用reduceByKey将提供更好的性能。
      *
      * 在每个映射器上执行本地合并，然后将结果发送给一个reducer，
      * 类似于MapReduce中的“combiner”。输出将使用现有的分区/并行级别进行哈希分区。
      * 总结：在对大数据进行复杂计算时，reduceByKey优于groupByKey。
      */

    /**
      * Transformations(转换算子)——————aggregate/aggregateByKey
      * aggregate:
      * 将每个分区里面的元素进行聚合，然后用combine函数将每个分区的结果和初始值(zeroValue)
      * 进行combine操作。这个函数最终返回的类型不需要和RDD中元素类型一致。
      * aggregateByKey:
      * 对PairRDD中相同的Key值进行聚合操作，在聚合过程中同样使用了一个中立的初始值。
      * 和aggregate函数类似，aggregateByKey返回值的类型不需要和RDD中value的类型一致。
      * 因为aggregateByKey是对相同Key中的值进行聚合操作，所以aggregateByKey'函数最终
      * 返回的类型还是PairRDD，对应的结果是Key和聚合后的值，而aggregate函数直接返回的是非RDD的结果。
      */
    //    data1.aggregate()

    //    val line1: RDD[(Int, Int)] = sc.parallelize(List((1,3),(1,2),(1,4),(2,3)), 2)
    //
    //    //合并不同partition中的值，a，b得数据类型为zeroValue的数据类型
    //    def combOp(a: String, b: String): String = {
    //      println("combOp: " + a + "\t" + b)
    //      a + b
    //    }
    //
    //    //合并在同一个partition中的值，a的数据类型为zeroValue的数据类型，b的数据类型为原value的数据类型
    //    def seqOp(a: String, b: Int): String = {
    //      println("SeqOp:" + a + "\t" + b)
    //      a + b
    //    }
    //
    //    val aggregateByKeyRDD = line1.aggregateByKey("100")(seqOp, combOp)
    //    aggregateByKeyRDD.foreach(println)

    //    val list = List(1, 2, 3, 4, 5, 6, 7, 8, 9)
    //    val (mul, sum, count) = sc.parallelize(list, 2).aggregate((1, 0, 0))(
    //      (acc, number) => (acc._1 * number, acc._2 + number, acc._3 + 1),
    //      (x, y) => (x._1 * y._1, x._2 + y._2, x._3 + y._3)
    //    )
    //    println("平均数和所有元素乘积: ")
    //    println(sum / count, mul)
    //
    //    val raw = List("a", "b", "d", "f", "g", "h", "o", "q", "x", "y")
    //    val (biggerthanf, lessthanf) = sc.parallelize(raw, 1).aggregate((0, 0))(
    //      (cc, str) => {
    //        var biggerf = cc._1
    //        var lessf = cc._2
    //        if (str.compareTo("f") >= 0) biggerf = cc._1 + 1
    //        else if(str.compareTo("f") < 0) lessf = cc._2 + 1
    //        (biggerf, lessf)
    //      },
    //      (x, y) => (x._1 + y._1, x._2 + y._2)
    //    )

    // 使用aggregate()进行求和与求积
    //    val line1 = sc.parallelize(List(1, 2, 3, 4, 5), 2)
    //    val (sum, mul) = line1.aggregate((0, 1))(
    //      (s, m) => (s._1 + m, s._2 * m) // 1.先基于初始值(0,1),对每个._1进行加操作，对每个._2进行乘操作，
    //      ,
    //      (x, y) => (x._1 + y._1, x._2 * y._2) // 2.再对上面步骤进行聚合，对每个加结果进行加聚合，对每个乘结果进行乘聚合。
    //    )
    //    println(sum, mul)
    //
    //    val line2 = sc.parallelize(List(1, 2, 3, 4, 5), 2)
    //    val (qiuhe, qiugeshu) = line2.aggregate((0, 0))(
    //      (sum, num) => {
    //        (sum._1 + num, sum._2 + 1)
    //      },
    //      (x, y) => (x._1 + y._1, x._2 + y._2)
    //    )
    //    println(qiuhe / qiugeshu)

    //    // 和aggregate类似，aggregateByKey是针对Key进行聚合操作
    //    val line1 = sc.parallelize(List((1, 1), (2, 2), (3, 3), (4, 4), (5, 5), (1, 6), (2, 7), (3, 8), (4, 9), (5, 10)), 2)
    //    val need: RDD[(Int, (Int, Int))] = line1.aggregateByKey((0, 1))(
    //      (s, m) => (s._1 + m, s._2 * m),
    //      (x, y) => (x._1 + y._1, x._2 * y._2)
    //    )
    //    need.foreach(println)

    /**
      * aggregate/aggregateByKey:
      * seqOp操作会聚合各分区中的元素，然后combOp操作把所有分区的聚合结果再次聚合，
      * 两个操作的初始值都是zeroValue.
      * seqOp的操作是遍历分区中的所有元素(T)，第一个T跟zeroValue做操作，
      * 结果再作为与第二个T做操作的zeroValue，直到遍历完整个分区。
      * combOp操作是把各分区聚合的结果，再聚合。aggregate函数返回一个跟RDD不同类型的值。
      * 因此，需要一个操作seqOp来把分区中的元素T合并成一个U，另外一个操作combOp把所有U聚合。
      * def aggregate[U: ClassTag](zeroValue: U)(seqOp: (U, T) => U, combOp: (U, U) => U): U = withScope {
      * // Clone the zero value since we will also be serializing it as part of tasks
      * var jobResult = Utils.clone(zeroValue, sc.env.serializer.newInstance())
      * val cleanSeqOp = sc.clean(seqOp)
      * val cleanCombOp = sc.clean(combOp)
      * val aggregatePartition = (it: Iterator[T]) => it.aggregate(zeroValue)(cleanSeqOp, cleanCombOp)
      * val mergeResult = (index: Int, taskResult: U) => jobResult = combOp(jobResult, taskResult)
      *     sc.runJob(this, aggregatePartition, mergeResult)
      * jobResult
      * }
      * 总结:
      * def aggregate[U: ClassTag](zeroValue: U)(seqOp: (U, T) => U, combOp: (U, U) => U): U
      * 这个函数是一个柯里化的函数，有两部分参数:(zeroValue: U)和(seqOp: (U, T);
      * aggregate先对每个分区的元素做聚集，然后对所有分区的结果做聚集，聚集过程中，
      * 使用的是给定的聚集函数以及初始值”zero value”。
      * 这个函数能返回一个与原始RDD不同的类型U，因此，需要一个合并RDD类型T到结果类型U的函数，
      * 还需要一个合并类型U的函数。这两个函数都可以修改和返回他们的第一个参数，
      * 而不是重新新建一个U类型的参数以避免重新分配内存。
      */

    /**
      * Transformations(转换算子)——————sortBy/sortByKey
      * sortBy:   底层源码其实还是调用的sortByKey()算子：
      * def sortBy[K](
      * f: (T) => K,
      * ascending: Boolean = true,
      * numPartitions: Int = this.partitions.length)
      * (implicit ord: Ordering[K], ctag: ClassTag[K]): RDD[T] = withScope {
      *     this.keyBy[K](f)
      * .sortByKey(ascending, numPartitions)
      * .values
      * }
      * 根据key对数据进行排序
      * sortByKey:
      * 按Key对数据进行排序，是sortBy的底层调用方法。
      */
    //    val line1 = sc.parallelize(List((1, 2), (3, 2), (2, 4), (5, 1), (4, 3)))
    //    line1.sortBy(_._1).foreach(println)
    //    line1.sortByKey(true,1).foreach(println)
    //    line1.sortBy(_._2).foreach(println)

    //    val line1 = sc.parallelize(List(("huyang", 1), ("huyangyang", 2), ("hudayang", 4), ("huxiaoyang", 3),("ceshi",9)), 1)
    //    line1.sortByKey(true, 1).foreach(println)

    /**
      * sortBy/sortByKey:
      * 总结:
      * sortBy底层调用sortByKey算子，主要参数有两个：
      * 一个是ascending: Boolean = true，默认为true，指定升/降序排列；
      * 一个是numPartitions: Int = self.partitions.length，默认是父rdd的分区数,指定分区数。
      * 一般来说，需要提前将K备好，然后直接调用sortByKey()算子,按K的顺序进行排列。
      * 汉字的k的排序是按照每个字的首字母排列；
      * 字母的k的排序是按照字母排序；
      */

    /**
      * Transformations(转换算子)——————join:
      * join 对两个需要连接的 RDD 进行 cogroup函数操作，将相同 key 的数据能够放到一个分区，
      * 在 cogroup 操作之后形成的新 RDD 对每个key 下的元素进行笛卡尔积的操作，返回的结果再展平，
      * 对应 key 下的所有元组形成一个集合。最后返回 RDD[(K， (V， W))]。
      */
    //    val line1 = sc.parallelize(List(("huyang", 1), ("dd", 4)))
    //    val line2 = sc.parallelize(List(("huyang", 3), ("bb", 2)))
    //
    //    val result: RDD[(String, (Int, Int))] = line1.join(line2)
    //    result.foreach(println)
    //    line2.join(line1).foreach(println)
    /**
      * join:
      * 总结:本质是通过cogroup()算子先进行协同划分,再通过flatMapValues将合并的数据打散。
      * 最终返回的是一个PairRDD。
      */

    /**
      * Transformations(转换算子)——————cogroup:
      * def cogroup[W](other: RDD[(K, W)]): RDD[(K, (Iterable[V], Iterable[W]))]
      *
      * def cogroup[W1, W2](other1: RDD[(K, W1)], other2: RDD[(K, W2)]): RDD[(K, (Iterable[V], Iterable[W1], Iterable[W2]))]
      *
      * def cogroup[W1, W2, W3](other1: RDD[(K, W1)], other2: RDD[(K, W2)], other3: RDD[(K, W3)]): RDD[(K, (Iterable[V], Iterable[W1], Iterable[W2], Iterable[W3]))]
      * cogroup相当于SQL中的全外关联full outer join，返回左右RDD中的记录，关联不上的为空。
      * 参数numPartitions用于指定结果的分区数。
      * 参数partitioner用于指定分区函数。
      */
    //    val line1 = sc.parallelize(List(("huyang", 1), ("dd", 4)))
    //    val line2 = sc.parallelize(List(("huyang", 3), ("bb", 2)))
    //    line1.cogroup(line2).foreach(println)

    /**
      * cogroup
      * 总结:返回RDD的内外数据，并且对能关联/匹配的进行组合，关联不上的保持为空。
      */


    /**
      * Transformations(转换算子)——————cartesian:
      * 求两个RDD的笛卡尔积
      */
    //    val line1 = sc.parallelize(List(1, 3, 5, 7, 9))
    //    val line2 = sc.parallelize(List(2, 4, 6, 8, 10))
    //    line1.cartesian(line2).foreach(println)

    /**
      * cartesian
      * 总结:对两个RDD的数据求笛卡尔积
      */

    /**
      * Transformations(转换算子)——————pipe:
      * 待定。
      */
    /**
      * pipe
      * 总结:
      */

    /**
      * Transformations(转换算子)——————coalesce/repartition:
      * 对RDD重新分区。
      * 都是RDD的分区进行重新划分，repartition只是coalesce接口中shuffle为true的简易实现，
      * （假设RDD有N个分区，需要重新划分成M个分区）
      * 1）N < M。一般情况下N个分区有数据分布不均匀的状况，利用HashPartitioner函数将数据重新分区为M个，
      * 这时需要将shuffle设置为true。
      * 2）如果N > M并且N和M相差不多，(假如N是1000，M是100)那么就可以将N个分区中的若干个分区合并成一个新的分区，
      * 最终合并为M个分区，这时可以将shuff设置为false，
      * 在shuffl为false的情况下，如果M>N时，coalesce为无效的，不进行shuffle过程，父RDD和子RDD之间是窄依赖关系。
      * 3）如果N > M并且两者相差悬殊，这时如果将shuffle设置为false，父子RDD是窄依赖关系，
      * 他们同处在一个stage中，就可能造成Spark程序的并行度不够，从而影响性能，如果在M为1的时候，
      * 为了使coalesce之前的操作有更好的并行度，可以讲shuffle设置为true。
      */
    //    val list = List(1 to 1000)
    //    val line1 = sc.parallelize(list, 100)
    //    val enen = line1.repartition(500)
    //    val enen2 = line1.coalesce(105)
    //    val enen3 = line1.repartition(10)
    //    println(enen3.getNumPartitions)
    /**
      * coalesce/repartition
      * 总结:原来分区N，后来分区M。
      * 当往大划分————N<M，使用repartition；
      * 当往稍小划分——N>=M,使用coalesce；
      * 当往特小划分——N>>M,使用repartition；
      */

    /**
      * Transformations(转换算子)——————repartitionAndSortWithinPartitions:
      * 如果需要在repartition重分区之后，还要进行排序，建议直接使用repartitionAndSortWithinPartitions算子。
      * 因为该算子可以一边进行重分区的shuffle操作，一边进行排序。
      * shuffle与sort两个操作同时进行，比先shuffle再sort来说，性能可能是要高的。
      */

    //    val list = List((1,1), (6,1), (4,1), (7,2), (8,2), (2,2), (3,3), (9,3), (5,3))
    //    val line1 = sc.parallelize(list, 2)
    //    line1.repartitionAndSortWithinPartitions(new Partitioner(){
    //      def numPartitioner:Int = 2
    //      def getPartitioner(key:Object) = key.toString.hashCode % numPartitioner
    //    })

    /**
      * coalesce/repartition
      * 总结:
      * def repartitionAndSortWithinPartitions(partitioner: Partitioner): RDD[(K, V)] = self.withScope {
      * new ShuffledRDD[K, V, V](self, partitioner).setKeyOrdering(ordering)
      * }
      * 从源码中可以看出，该方法依据partitioner对RDD进行分区，并且在每个结果分区中按key进行排序；
      * 通过对比sortByKey发现，这种方式比先分区，然后在每个分区中进行排序效率高，这是因为它可以将排序融入到shuffle阶段。
      */

    sc.stop()

  }

  // 封装mapPartitionsWithIndex的一个方法。
  def mapPartIndexFunc(i1: Int, iter: Iterator[Int]): Iterator[(Int, Int)] = {
    var res = List[(Int, Int)]()
    while (iter.hasNext) {
      val next = iter.next()
      res = res.::(i1, next)
    }
    res.iterator
  }


}
