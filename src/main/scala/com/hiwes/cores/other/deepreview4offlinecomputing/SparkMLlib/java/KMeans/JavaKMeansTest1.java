package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.java.KMeans;

import java.io.Serializable;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.sql.SparkSession;

//SparkSession版本的 KMeans算法示例
public class JavaKMeansTest1 implements Serializable{

	public static void main(String[] args) {
		SparkSession session = SparkSession
				.builder()
				.appName("JavaKMeansTest1")
				.master("spark://hiwes:7077")
				.getOrCreate();

		String path = "/opt/kmeans_data.txt";
		JavaRDD<String> data =  session.read().textFile(path).javaRDD();
		//以上操作全部是读文件的操作
		//下面的操作才是对数据集的操作
		JavaRDD<Vector> parsedData = data.map(s -> {  //返回的还是一个集合
		      String[] sarray = s.split(" ");
		      double[] values = new double[sarray.length];
		      for (int i = 0; i < sarray.length; i++) {
		        values[i] = Double.parseDouble(sarray[i]);
		      }
		      return Vectors.dense(values);
		    });
		    parsedData.cache();//实际上是parsedData.persist(StorageLevel.MEMORY_ONLY)      缓存的一种

		    int numClusters = 2;
		    int numIterations = 20;
		    KMeansModel clusters = KMeans.train(parsedData.rdd(), numClusters, numIterations);
		    for(Vector center:clusters.clusterCenters()) {
		    	System.out.println(" " + center);
		    }
		    double cost = clusters.computeCost(parsedData.rdd());
		    System.out.println("Cost: " + cost);

		    double WSSSE = clusters.computeCost(parsedData.rdd());
		    System.out.println("Within Set Sum of Squared Errors = " + WSSSE);

		    clusters.save(session.sparkContext(), "target/org/apache/spark/JavaKMeansExample/KMeansModel");

		    KMeansModel sameModel = KMeansModel.load(session.sparkContext(), "target/org/apache/spark/JavaKMeansExample/KMeansModel");

		    for(Vector c:sameModel.clusterCenters()) {
		    	System.out.println(c);
		    }
	}

}