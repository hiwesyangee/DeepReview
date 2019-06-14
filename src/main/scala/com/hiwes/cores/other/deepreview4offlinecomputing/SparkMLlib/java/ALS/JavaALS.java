package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.java.ALS;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

//ALS算法实现代码
public class JavaALS implements Serializable {
    private static final long serialVersionUID = 1L;

    //第一个展开类
    static class ParseRating implements Function<String, Rating> {
        private static final long serialVersionUID = 1L;
        private static final Pattern COMMA = Pattern.compile(",");

        @Override
        public Rating call(String line) {
            String[] tok = COMMA.split(line);
            int x = Integer.parseInt(tok[0]);
            int y = Integer.parseInt(tok[1]);
            double rating = Double.parseDouble(tok[2]);
            return new Rating(x, y, rating);
        }
    }

    //第二个展开类
    static class FeaturesToString implements Function<Tuple2<Object, double[]>, String> {
        private static final long serialVersionUID = 1L;

        @Override
        public String call(Tuple2<Object, double[]> element) {
            return element._1() + "," + Arrays.toString(element._2());
        }
    }

    public void run(String[] args) {
        // 建立SparkSession连接
        SparkSession session = SparkSession
                .builder()
                .appName("JavaALS")
                .master("spark://hiwes:7077")
                .getOrCreate();

        int rank = Integer.parseInt(args[1]);
        int iterations = Integer.parseInt(args[2]);
        String outputDir = args[3];
        int blocks = 1;//修改参数为1，而不是-1，定义分区数量
        if (args.length == 5) {
            blocks = Integer.parseInt(args[4]);
        }

        // 创建JavaRDD读取文件
        JavaRDD<String> lines = session.read().textFile(args[0]).javaRDD();
        JavaRDD<Rating> ratings = lines.map(new ParseRating());

        MatrixFactorizationModel model = ALS.train(ratings.rdd(), rank, iterations, 0.01, blocks);
        model.userFeatures().toJavaRDD().map(new FeaturesToString()).saveAsTextFile(outputDir + "/userFeatures");
        model.productFeatures().toJavaRDD().map(new FeaturesToString()).saveAsTextFile(outputDir + "/productFeatures");
        System.out.println("Final user/product features written to " + outputDir);

        JavaRDD<Tuple2<Object, Object>> userProducts = ratings.map(r -> new Tuple2<>(r.user(), r.product()));

        JavaPairRDD<Tuple2<Integer, Integer>, Double> predictions = JavaPairRDD.fromJavaRDD(
                model.predict(JavaRDD.toRDD(userProducts)).toJavaRDD()
                        .map(r -> new Tuple2<>(new Tuple2<>(r.user(), r.product()), r.rating()))
        );

        List<Tuple2<Tuple2<Integer, Integer>, Double>> output = predictions.collect();
        for (Tuple2<?, ?> tuple : output) {
            System.out.println(tuple._1() + ": " + tuple._2());
        }
    }
}

