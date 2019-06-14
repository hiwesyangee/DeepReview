package com.yangee.cores.project.IntelligentCustomerSystem.mongodb.test;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用com.mongodb.client.MongoCollection类的 insertMany() 方法来插入一个文档.
 */
public class TestMongoDB {

    //    @Test
//    public void testMongoDB() {
//        // 创建MongoDB客户端
//        MongoClient hiwes = new MongoClient("hiwes", 27017);
//        Iterator<DB> test = hiwes.getUsedDatabases().iterator();
//
//        Mongo mongo = new Mongo("hiwes", 27017);
//        DB db = mongo.getDB("test");
//
//        Set<String> collectionNames = db.getCollectionNames();
//
//        for (String collect : collectionNames) {
//            System.out.println("collect: " + collect);
//        }
//    }
    public static void main(String[] args) {
        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient("hiwes", 27017);
            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("mycol");
            System.out.println("Connect to database successfully");

            MongoCollection<Document> collection = mongoDatabase.getCollection("test");
            System.out.println("集合 test 选择成功");

            //插入文档
            /**
             * 1. 创建文档 org.bson.Document 参数为key-value的格式
             * 2. 创建文档集合List<Document>
             * 3. 将文档集合插入数据库集合中 mongoCollection.insertMany(List<Document>) 插入单个文档可以用 mongoCollection.insertOne(Document)
             * */
            Document document = new Document("title", "MongoDB").
                    append("description", "database").
                    append("likes", 100).
                    append("by", "Fly");
            List<Document> documents = new ArrayList<>();
            documents.add(document);
            collection.insertMany(documents);
            System.out.println("文档插入成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
