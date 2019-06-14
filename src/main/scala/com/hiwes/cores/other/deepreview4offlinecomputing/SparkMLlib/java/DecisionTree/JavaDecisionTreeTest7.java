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

import scala.Tuple2;

public class JavaDecisionTreeTest7 implements Serializable {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("JavaDecisionTreeTest");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        String path1 = "file:///Users/hiwes/data/mllib/test.txt";
        String path2 = "file:///Users/hiwes/data/mllib/test2.txt";

        JavaRDD<LabeledPoint> data = MLUtils.loadLabeledPoints(jsc.sc(), path1).toJavaRDD();
        JavaRDD<LabeledPoint> data2 = MLUtils.loadLabeledPoints(jsc.sc(), path2).toJavaRDD();

        // 将数据分成培训和测试集(30%用于测试)
        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.7, 0.3});
        JavaRDD<LabeledPoint> trainingData = splits[0];
        JavaRDD<LabeledPoint> testData = splits[1];
        // 设置参数。空的分类功能表明所有的特性都是连续的。
        int numClasses = 20;
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
        String impurity = "gini";
        int maxDepth;
        int maxBins;
        DecisionTreeModel model0 = DecisionTree.trainClassifier(trainingData, numClasses, categoricalFeaturesInfo,
                impurity, 2, 10);

        data2.foreach(l -> model0.predict(l.features()));

        JavaPairRDD<Double, Double> predictionAndLabel02 = data2
                .mapToPair(p -> new Tuple2<>(model0.predict(p.features()), p.label()));
        double testError = predictionAndLabel02.filter(pl -> !pl._1().equals(pl._2())).count() / (double) data2.count();
        // 培训决策树模型进行分类。
        for (maxDepth = 2; maxDepth <= 100; maxDepth++) {
            for (maxBins = 10; maxBins <= 100; maxBins++) {
                DecisionTreeModel model = DecisionTree.trainClassifier(trainingData, numClasses,
                        categoricalFeaturesInfo, impurity, maxDepth, maxBins);

                // 对测试数据进行生成最小错误率
                JavaPairRDD<Double, Double> predictionAndLabel2 = data2
                        .mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));
                double dTest = predictionAndLabel2.filter(pl -> !pl._1().equals(pl._2())).count()
                        / (double) testData.count();
                if (testError > dTest) {
                    testError = dTest;
                }
            }
        }
        System.out.println("testError: " + testError);

    }
}
