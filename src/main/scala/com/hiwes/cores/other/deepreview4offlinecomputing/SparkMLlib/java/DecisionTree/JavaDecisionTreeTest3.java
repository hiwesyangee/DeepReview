package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.java.DecisionTree;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

public class JavaDecisionTreeTest3 implements Serializable {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("JavaDecisionTreeTest");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        // 加载和解析数据文件
        String path = "file:///Users/hiwes/data/mllib/test.txt";
        JavaRDD<LabeledPoint> data = MLUtils.loadLabeledPoints(jsc.sc(), path).toJavaRDD();
        // 将数据分成培训和测试集(30%用于测试)
        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.7, 0.3});
        JavaRDD<LabeledPoint> trainingData = splits[0];
        JavaRDD<LabeledPoint> testData = splits[1];

        // 设置参数。
        // 空的分类功能表明所有的特性都是连续的。
        int numClasses = 20;
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
        String impurity = "gini";
        int Depth;
        int Bins = 0;        //分支中的元素个数是多少
        double minTestError = run(numClasses, trainingData, categoricalFeaturesInfo, impurity, 2, 10, testData);
        for (Depth = 2; Depth <= 50; Depth++) {
            for (Bins = 10; Bins <= 200; Bins++) {
                double d = run(numClasses, trainingData, categoricalFeaturesInfo, impurity, Depth, Bins, testData);
                if (minTestError > d) {
                    minTestError = d;
                }
            }
            if (minTestError == run(numClasses, trainingData, categoricalFeaturesInfo, impurity, Depth, Bins, testData)) {
                break;
            }
        }
        System.out.println("minTestError:" + minTestError + ",Depth:" + Depth + ",Bins:" + Bins);
    }

    // 决策树方法
    public static double run(int numClasses, JavaRDD<LabeledPoint> trainingData, Map<Integer, Integer> categoricalFeaturesInfo,
                             String impurity, int maxDepth, int maxBins, JavaRDD<LabeledPoint> testData) {
        DecisionTreeModel model = DecisionTree.trainClassifier(trainingData, numClasses, categoricalFeaturesInfo, impurity, maxDepth, maxBins);

        // 对测试实例进行评估，并计算测试错误
        JavaPairRDD<Double, Double> predictionAndLabel = testData.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));

        double testErr = predictionAndLabel.filter(pl -> !pl._1().equals(pl._2())).count() / (double) testData.count();
        return testErr;
    }
}
