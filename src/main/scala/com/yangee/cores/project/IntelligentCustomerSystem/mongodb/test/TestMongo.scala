package com.yangee.cores.project.IntelligentCustomerSystem.mongodb.test

import com.mongodb.{DB, MongoClient}

object TestMongo {
  def main(args: Array[String]): Unit = {
    try {
      val mongoClient = new MongoClient("hiwes", 27017)

      // 连接到数据库
      val mongoDatabase: DB = mongoClient.getDB("test")


    } catch {
      case e: Exception => e.printStackTrace()
    }

  }
}
