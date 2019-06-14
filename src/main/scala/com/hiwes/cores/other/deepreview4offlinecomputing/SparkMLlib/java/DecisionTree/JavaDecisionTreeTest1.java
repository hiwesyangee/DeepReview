package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.java.DecisionTree;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Scanner;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

/**
 * 本次要求如下，包含步骤进行分类： 1.将行车记录数据进行筛选(
 * 找出19-21点之间的所有数据，按照经纬度积累成20个类别。意思是说——用filter筛选出符合要求是的数据，并定义k =
 * 20；使用聚类算法K-Means，得到结果1——————建立模型，并得到20个中心点。假定此时的数据还有3万条 )
 * 此时做一步操作，将每一条数据来自哪一类做出判断，并在前面(或后面)加上字符串 也可以把属于哪一类作为K，把经纬度设为V，这样存在RDD里面也可以。
 * 2.假定此时的数据有3万条，那么拿出500条数据作为测试数据（这五百条作为测试数据，只需要经纬度，不需要知道它们属于哪一个类）。剩下的29500条数据用来建模和测试（70%建模，30%测试），用决策树算法得到结果2——————决策树模型。用30%取验证准确率有多高。算正确率多高
 * 得到一个百分比。 3.将刚刚的500条数据再进行测试，与刚刚得到的结果进行比对，得到一个结果3——————符合要求的百分比 4.打印出百分比
 */

public class JavaDecisionTreeTest1 implements Serializable {
    public static Vector parseVector(String s) {
        String[] array = s.split(",");
        double[] values = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            values[i] = Double.parseDouble(array[i]);
        }
        return Vectors.dense(values);
    }


    public static void main(String[] args) throws Exception {
        SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("JavaDecisionTreeTest");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        String path = "file:///Users/hiwes/data/mllib/Allearcitibike.txt";
        JavaRDD<String> data = jsc.textFile(path);
        // 1.找出符合条件的数据（19-21点之间的所有数据，用filter）
//        JavaRDD<String> parsedData = data.filter(new Function<String, Boolean>() {
//            private static final long serialVersionUID = 7204654907746585581L;
//
//            public Boolean call(String s) throws Exception {
//                String[] arr = s.split(",");
//                String[] array1 = (arr[1].split(" "))[1].split(":");
//                if (Integer.parseInt(array1[0]) >= 19 && Integer.parseInt(array1[0]) <= 21) {
//                    return true;
//                }
//                return false;
//            }
//        });
        JavaRDD<String> parsedData = data.filter(s -> {
            String[] arr = s.split(",");
            String[] array1 = (arr[1].split(" "))[1].split(":");
            if (Integer.parseInt(array1[0]) >= 19 && Integer.parseInt(array1[0]) <= 21) {
                return true;
            }
            return false;
        });
        // 用来测试找的数据到底是不是符合要求的
        // List<String> collect1 = parsedData.collect();
        // for(String s: collect1) {
        // System.out.println(s);
        // }
//        JavaRDD<String> parsedData2 = parsedData.map(new Function<String, String>() {
//            private static final long serialVersionUID = 1803044614933056580L;
//
//            public String call(String s) throws Exception {
//                String[] arr = s.split(",");
//                return arr[5] + "," + arr[6];
//            }
//        });

        // 2.将目标文件通过筛选，得到需要的经纬度
        JavaRDD<String> parsedData2 = parsedData.map(s -> {
            String[] arr = s.split(",");
            return arr[5] + "," + arr[6];
        });

        // 3.建立JavaRDD<Vector>得到结果用来传入K—means算法
        JavaRDD<Vector> parsedData3 = parsedData2.map(s -> parseVector(s)).cache();

        List<Vector> collect3 = parsedData3.collect();

        // 4.使用K—Means算法。
        int numClusters = 20;
        int numIterations = 20;
        KMeansModel cluster = KMeans.train(parsedData3.rdd(), numClusters, numIterations);
        // 5.将每条记录属于哪一类,写到txt文件中去,类别写到字符串末尾，样子就是"纬度，经度，类别"。
        // List<Vector> allData = parsedData3.collect();
        String newData = ""; // 创建空字符串用来进行字符串拼接
        for (Vector v : collect3) {
            int number = cluster.predict(v);
            newData += String.valueOf(number) + "," + v.toString().substring(1, v.toString().length() - 1) + "\n";
        }
        System.out.println(newData);

        Scanner input = new Scanner(newData);
        FileOutputStream fos = new FileOutputStream("file:///Users/hiwes/data/mllib/test.txt");
        while (input.hasNext()) {
            String a = input.next();
            fos.write((a + "\r\n").getBytes());
        }
        fos.close();
        input.close();
    }
}