package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.java.FPGrowth;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowthModel;
import org.apache.spark.mllib.fpm.FPGrowth.FreqItemset;
import org.apache.spark.sql.SparkSession;
import org.spark_project.guava.base.Joiner;
import org.spark_project.guava.collect.Lists;
//FP—Growth算法实现代码

public class JavaFPGrowth implements Serializable {
    public void run(String[] args) {
        String inputFile = args[0];
        double minSupport = 0.3;
        int numPatition = 1;//修改参数为1，而不是-1，定义分区数量

        SparkSession sparkSession = SparkSession
                .builder()
                .appName("JavaFPGrowth")
                .master("spark://hiwes:7077")
                .getOrCreate();
        sparkSession.sparkContext().addJar("/opt/test.jar");

        JavaRDD<String> line = sparkSession.read().textFile(inputFile).javaRDD();
        JavaRDD<ArrayList<String>> transactions = line.map(s -> Lists.newArrayList(s.split(" ")));

        FPGrowthModel<String> model = new FPGrowth()
                .setMinSupport(minSupport)
                .setNumPartitions(numPatition)
                .run(transactions);

        for (FreqItemset<String> s : model.freqItemsets().toJavaRDD().collect()) {
            System.out.println("[" + Joiner.on(",").join(s.javaItems()) + "]," + s.freq());
        }

        System.out.println("Number of frequent itemsets: ${model.freqItemsets.count()}");
        model.freqItemsets().toJavaRDD().foreach(itemset -> {
                    System.out.println("[" + Joiner.on(",").join(itemset.javaItems()) + "]," + itemset.freq());
                }
        );
    }
}

