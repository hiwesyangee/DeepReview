package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.java.KMeans;

import java.io.Serializable;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.sql.SparkSession;

/**
 * 1.读取数据————一直到JavaRDD<String> data 创建成功。
 * 2.从第二行开始分析
 * 把第2个元素提出来，		起始时间：	2016-10-01 00:00:38，
 * 把第6，7个元素提出来，	起始经纬度：	[40.72333158646436,-74.04595255851744]
 * 此处证明，我目前还没有能力解决涉及经纬度的计算，所以此处应当提取出起始站的编号
 * arr[3]:	第4个元素	起始站编号
 * <p>
 * 得到的RDD是字符串"2016-10-01 00:00:38，40.72333158646436,-74.04595255851744"
 * <p>
 * 3.根据这两个特点进行建模，假设计算骑车时间最多的两个小时(前后各一个小时)，经纬度上下浮动0.001的区域是需求区域。
 * 4.定义聚类个数为3，迭代次数为20。找出符合条件的时间和经纬度
 */

/**
 *    @Notes 这是基于2016单车行驶记录寻找用车时间和地点最频繁的第一个实验版本。
 * 					个人感觉非常不好做，问题在于有两个特点需要考虑有点不知所措.在找出了
 * 					需要的信息并存入RDD之后，问题是怎么把这两个参数进行分割存放。。。
 * 					最后还是决定用经纬度，所以还是要用第6，7个元素进行，相当于用三个特点做。。。
 *	@author Hiwes Yangee
 *	@version 1.0
 *	@data 12.14.2017
 */
public class JavaKMeansTest2 implements Serializable {
    public static void main(String[] args) {
        //1.读取数据————一直到JavaRDD<String> data 创建成功。
        SparkSession session = SparkSession
                .builder()
                .appName("JavaKMeansTest2")
                .master("spark://hiwes:7077")
                .getOrCreate();

        String path = "/opt/Allearcitibike.txt";
        JavaRDD<String> data = session.read().textFile(path).javaRDD();

		/*2.从第二行开始分析。把第2个元素提出来，		起始时间：	2016-10-01 00:00:38，
		 * 															截取以空格划分的第二个元素，再以冒号划分的第1个元素。得到整数点时间：00
	   										把第4个元素提出来，		起始站编号：3186
		*/
        JavaRDD<String> parsedData1 = data.map(s -> {
            String[] arr = s.split(",");
            String[] arr2 = (arr[1].split(" "))[1].split(":");
            return arr2[0] + " " + arr[5] + " " + arr[6];
        });

        JavaRDD<Vector> parsedData2 = parsedData1.map(s -> {
            String[] sarray = s.split(" ");
            double[] values = new double[sarray.length];
            for (int i = 0; i < sarray.length; i++) {
                values[i] = Double.parseDouble(sarray[i]);
            }
            return Vectors.dense(values);
        });
        parsedData2.cache();//实际上是parsedData.persist(StorageLevel.MEMORY_ONLY)      缓存的一种

        //K-means算法内容
        int numClusters = 2;
        int numIterations = 20;
        KMeansModel clusters = KMeans.train(parsedData2.rdd(), numClusters, numIterations);

        for (Vector center : clusters.clusterCenters()) {
            System.out.println("hello：" + center);
        }
        double cost = clusters.computeCost(parsedData2.rdd());
        System.out.println("Cost: " + cost);

        double WSSSE = clusters.computeCost(parsedData2.rdd());
        System.out.println("Within Set Sum of Squared Errors = " + WSSSE);

        for (Vector c : clusters.clusterCenters()) {
            System.out.println(c);
        }
        //找出100条记录，判断它们属于哪一个标签。每一条记录相当于模型中心点的子集
        System.out.println("-----------开始判断过程-----------");
        //1.取100条记录：for()循环
        List<Vector> top = parsedData2.take(100);//此处top无法匹配，因为后面返回的是Spark的 org.apache.spark.mllib.linalg.Vector

        for (int i = 0; i < 100; i++) {
            //2.判断每一条记录属于哪一类：if语句
            Vector vector = top.get(i);
            switch (clusters.predict(vector)) {
                case 0:
                    //3.打印出每一条记录属于哪一类
                    System.out.println(vector + "belongs to:" + clusters.clusterCenters()[0]);
                    break;
                default:
                    System.out.println(vector + "belongs to:" + clusters.clusterCenters()[1]);
            }
        }
        System.out.println("-----------结束判断过程-----------");
        session.stop();
    }
}
