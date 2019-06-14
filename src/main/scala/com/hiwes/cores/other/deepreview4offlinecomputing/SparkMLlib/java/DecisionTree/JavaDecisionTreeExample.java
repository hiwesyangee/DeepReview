package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.java.DecisionTree;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class JavaDecisionTreeExample {

    private static String allTaxiDataPath = "file:///Users/hiwes/data/mllib/AlltaxiDatas.txt";
    private static String kmeansDataPath = "file:///Users/hiwes/data/mllib/kmeansData.txt";
    private static String decisionModelPath = "file:///Users/hiwes/data/model/decisionTree/bike/DecisionTreeModel";


    // 实现字符串变量化
    public static Vector parseVector(String s) {
        String[] array = s.split(",");
        double[] values = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            values[i] = Double.parseDouble(array[i]);
        }
        return Vectors.dense(values);
    }

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("JavaDecisionTreeExample").setMaster("local[2]");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        /**
         * 读取本地数据文件，将数据文件进行筛选过滤，提取晚上19-21点之间的行车数据，并获取上车经纬度
         * 将经纬度进行聚类，计算结果写入本地文件。
         */
        // 0.获取所有数据
        JavaRDD<String> allDataRDD = jsc.textFile(allTaxiDataPath);

        // 1.过滤时间，获取19点-21点间的出行记录
        JavaRDD<String> periodData = allDataRDD.filter(s -> {
            String[] arr = s.split(",");
            String[] array1 = (arr[1].split(" "))[1].split(":");
            return (Integer.parseInt(array1[0]) >= 19 && Integer.parseInt(array1[0]) <= 21);
        }).persist(StorageLevel.MEMORY_AND_DISK());

        // 2.将目标文件通过筛选，得到需要的经纬度
        JavaRDD<String> latitudeAndLongitudeData = periodData.map(s -> {
            String[] arr = s.split(",");
            return arr[5] + "," + arr[6];
        });

        // 3.建立JavaRDD<Vector>得到结果用来传入K—means算法
        JavaRDD<Vector> VectorData = latitudeAndLongitudeData.map(s -> parseVector(s)).persist(StorageLevel.MEMORY_AND_DISK());

        List<Vector> collect3 = VectorData.collect();

        // 4.使用K—Means算法，创建KMeans模型。
        int bestNumClusters = 0;
        int bestNumIterations = 0;
        double bestWSSSE = 1.0;
        for (int k = 10; k <= 30; k++) {
            for (int i = 15; i <= 25; i++) {
                KMeansModel kmeansmodel = KMeans.train(VectorData.rdd(), k, i);
                double WSSSE = kmeansmodel.computeCost(VectorData.rdd());
                if (WSSSE < bestWSSSE) {
                    bestWSSSE = WSSSE;
                    bestNumClusters = k;
                    bestNumIterations = i;
                }
            }
        }

        KMeansModel bestKmeansmodel = KMeans.train(VectorData.rdd(), bestNumClusters, bestNumIterations);
        System.out.println("The minimum loss function is: " + bestWSSSE);

        // 5.实现字符串的本地化文件保存
        String newData = ""; // 创建空字符串用来进行字符串拼接
        for (Vector v : collect3) {
            int number = bestKmeansmodel.predict(v);
            newData += String.valueOf(number) + "," + v.toString().substring(1, v.toString().length() - 1) + "\n";
        }
        try {
            Scanner input = new Scanner(newData);
            FileOutputStream fos = new FileOutputStream(kmeansDataPath);
            while (input.hasNext()) {
                String a = input.next();
                fos.write((a + "\r\n").getBytes());
            }
            fos.close();
            input.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }

        periodData.unpersist();
        VectorData.unpersist();
        
        /**
         * 加载kmeans文件，并将数据切分，20%用作测试数据，80%作为训练数据用来生成Decision模型
         */
        // 加载和解析数据文件
        JavaRDD<LabeledPoint> data = MLUtils.loadLabeledPoints(jsc.sc(), kmeansDataPath).toJavaRDD();
        // 将数据分成培训和测试集(20%用于测试)
        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.8, 0.2});
        JavaRDD<LabeledPoint> trainingData = splits[0];
        JavaRDD<LabeledPoint> testData = splits[1];

        // 设置参数。
        // 空的分类功能表明所有的特性都是连续的。
        int numClasses = 20;
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
        String impurity = "gini";
        int numDepth = 2;
        int numBins = 10;        //分支中的元素个数是多少
        double testError = run(trainingData, numClasses, categoricalFeaturesInfo, impurity, numDepth, numBins, testData);
        for (int Depth = 2; Depth <= 20; Depth++) {
            for (int Bins = 10; Bins <= 100; Bins++) {
                double testErr = run(trainingData, numClasses, categoricalFeaturesInfo, impurity, Depth, Bins, testData);
                if (testErr < testError) {
                    testError = testErr;
                    numDepth = Depth;
                    numBins = Bins;
                }
            }
        }
        int resultDepth = numDepth;
        int resultBins = numBins;
        DecisionTreeModel decisionTreeModel = DecisionTree.trainClassifier(trainingData, numClasses, categoricalFeaturesInfo, impurity, resultDepth, resultBins);
        double minTestError = run(trainingData, numClasses, categoricalFeaturesInfo, impurity, resultDepth, resultBins, testData);
        System.out.println(minTestError);

        decisionTreeModel.save(jsc.sc(), decisionModelPath);

        jsc.close();
    }

    // 决策树方法
    public static double run(JavaRDD<LabeledPoint> trainingData, int numClasses, Map<Integer, Integer> categoricalFeaturesInfo,
                             String impurity, int maxDepth, int maxBins, JavaRDD<LabeledPoint> testData) {
        DecisionTreeModel model = DecisionTree.trainClassifier(trainingData, numClasses, categoricalFeaturesInfo, impurity, maxDepth, maxBins);
        // 对测试实例进行评估，并计算测试错误
        JavaPairRDD<Double, Double> predictionAndLabel = testData.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));

        double testErr = predictionAndLabel.filter(pl -> !pl._1().equals(pl._2())).count() / (double) testData.count();
        return testErr;
    }
}
