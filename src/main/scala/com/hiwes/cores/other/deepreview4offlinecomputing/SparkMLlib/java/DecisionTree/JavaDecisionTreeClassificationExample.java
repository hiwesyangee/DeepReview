package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.java.DecisionTree;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.util.HashMap;
import java.util.Map;

public class JavaDecisionTreeClassificationExample {

    public static void main(String[] args) {
        // 创建SparkSession连接，标准套路
        SparkSession session = SparkSession
                .builder()
                .appName("JavaDecisionTreeClassificationExample")
                .master("local[2]")
                .getOrCreate();

        // 加载和解析数据文件
        String datapath = "file:///Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/data/mllib/sample_libsvm_data.txt";
        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(session.sparkContext(), datapath).toJavaRDD();

        // 将数据分成培训和测试集(30%用于测试)
        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.7, 0.3});
        JavaRDD<LabeledPoint> trainingData = splits[0];
        JavaRDD<LabeledPoint> testData = splits[1];

        // 设置参数。
        // 空的分类功能表明所有的特性都是连续的。
        int numClasses = 2;
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
        String impurity = "gini";
        int maxDepth = 5;
        int maxBins = 32;

        // 培训决策树模型进行分类。
        DecisionTreeModel model = DecisionTree.trainClassifier(trainingData, numClasses, categoricalFeaturesInfo,
                impurity, maxDepth, maxBins);
        // 对测试实例进行评估，并计算测试错误
        JavaPairRDD<Double, Double> predictionAndLabel = testData
                .mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));
        double testErr = predictionAndLabel.filter(pl -> !pl._1().equals(pl._2())).count() / (double) testData.count();


        System.out.println("Test Error: " + testErr);
        System.out.println("Learned classification tree model:\n" + model.toDebugString());

        session.stop();
    }

}