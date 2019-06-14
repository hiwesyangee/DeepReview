package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.java.DecisionTree;

import java.io.Serializable;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

public class JavaDecisionTreeTest5 implements Serializable {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("JavaDecisionTreeTest");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        // 加载和解析数据文件
        String path = "/opt/test3.txt";
        JavaRDD<LabeledPoint> testData = MLUtils.loadLabeledPoints(jsc.sc(), path).toJavaRDD();

        //下载原本测试数据创建的模型进行验证
        DecisionTreeModel model = DecisionTreeModel.load(jsc.sc(), "file:///Users/hiwes/data/model/decisionTree/bike/DecisionTreeModel");
        // 对测试实例进行评估，并计算测试错误
        JavaPairRDD<Double, Double> predictionAndLabel = testData
                .mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));

        double testErr2 = predictionAndLabel.filter(pl -> !pl._1().equals(pl._2())).count() / (double) testData.count();

        System.out.println("Test Error: " + testErr2);
//		System.out.println("Learned classification tree model:\n" + model.toDebugString());
    }

}
