package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.java.FPGrowth;

//Java FP-tree算法测试
public class JavaFPGrowthTest {

	public static void main(String[] args) {
		JavaFPGrowth fp = new JavaFPGrowth();
		String[] arr = {"file:///Users/hiwes/app/spark-2.2.0-bin-2.6.0-cdh5.7.0/data/mllib/sample_fpgrowth.txt"};
		fp.run(arr);
	}
}
