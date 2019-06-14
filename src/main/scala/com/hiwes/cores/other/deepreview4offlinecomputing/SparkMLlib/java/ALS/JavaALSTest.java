package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.java.ALS;

//JavaALS算法测试
public class JavaALSTest {
    public static void main(String[] args) {
        JavaALS al = new JavaALS();
        String[] arr = {"file:///Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/data/mllib/als/test.data", "10", "20", "file:///Users/hiwes/data/mllib/als"};
        al.run(arr);
    }
}
