package com.hiwes.cores.other.deepreview4offlinecomputing.SparkCore.Java;//package com.hiwes.cores.deepreview4offlinecomputing.SparkCore.Java;
//
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaPairRDD;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.api.java.function.FlatMapFunction;
//import org.apache.spark.api.java.function.Function;
//import org.apache.spark.api.java.function.PairFunction;
//import scala.Tuple2;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.List;
//
///**
// * 深度复习之离线计算1:Spark Core（Java版本）
// *
// * @author Hiwes
// * @version 1.0
// * @since 2018/8/30 上午11:00
// */
//
//public class JavaSparkCoreTest {
//    private static List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
//    private static String path = "/Users/hiwes/data/test.txt";
//    private static String hdfsPath = "hdfs://hiwes:8020/data/hdfsTest.txt";
//
//    public static void main(String[] args) {
//        // 初始化Spark连接
//        SparkConf conf = new SparkConf().setMaster("local[4]").setAppName("Test");
//        JavaSparkContext jsc = new JavaSparkContext(conf);
//
//        // SparkCore三种方式创建RDD
//        JavaRDD<Integer> data1 = jsc.parallelize(list);
//        JavaRDD<String> data2 = jsc.textFile(path);
//        JavaRDD<String> data3 = jsc.textFile(hdfsPath);
//
//        /**
//         * Transformations(转换算子)
//         */
////        // map:实现每个元素加一
////        JavaRDD<Integer> transRdd1 = data1.map(x -> x + 1);
////        transRdd1.foreach(x -> {
////            System.out.println(x);
////        });
////
////        // flatMap:实现字符分割
////        JavaRDD<String> transRdd2 = data2.flatMap(
////                s -> Arrays.asList(s.split(",")).iterator());
////        transRdd2.foreach(s -> System.out.println(s));
//
//        // mapPartitions:实现分区转换
////        JavaRDD<Integer> transRdd3 = data3.flatMap(s -> Arrays.asList(s.split(",")).iterator())
////                .mapPartitions((FlatMapFunction<Iterator<String>, Integer>) ite -> {
////                    // 使用另一个ArrayList来进行数据存储，并返回迭代器
////                    ArrayList<Integer> arr = new ArrayList<>();
////                    while (ite.hasNext()) {
////                        int num = Integer.parseInt(ite.next()) + 1;
////                        arr.add(num);
////                    }
////                    return arr.iterator();
////                });
////        transRdd3.foreach(x -> System.out.println(x));
//
//        // mapPartitionsWithIndex:实现分区转换并携带分区索引：
////        data1.mapPartitionsWithIndex((i1, ite) -> {
////            ArrayList<Tuple2<Integer, Integer>> list = new ArrayList<>();
////            while (ite.hasNext()) {
////                int elemenet = ite.next();
////                list.add(new Tuple2<>(i1, elemenet));
////            }
////            return list.iterator();
////        }, false).foreach(s -> System.out.println(s));
//
////        // 返回抽样子集
////        JavaRDD<Integer> transRdd1 = data1.sample(false, 0.4); // false的情况下，第二参数是
////        JavaRDD<Integer> transRdd2 = data1.sample(true, 5); // true的情况下，第二参数是被选中的期望次数
//
////        JavaRDD<Integer> line1 = jsc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
////        JavaRDD<Integer> line2 = jsc.parallelize(Arrays.asList(5, 4, 3, 2, 1));
////        JavaRDD<Integer> line3 = line1.union(line2);
////        line3.foreach(x -> System.out.println(x));
//
//        JavaRDD<Integer> line1 = jsc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
//        JavaPairRDD<Integer, Integer> pair1 = line1.mapToPair(new PairFunction<Integer, Integer, Integer>() {
//            @Override
//            public Tuple2<Integer, Integer> call(Integer i) throws Exception {
//                return new Tuple2<>(i, 1);
//            }
//        });
//
//
//        jsc.stop();
//    }
//
//}
