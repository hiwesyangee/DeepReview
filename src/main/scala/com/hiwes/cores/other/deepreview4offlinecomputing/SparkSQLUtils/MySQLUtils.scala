//package com.hiwes.cores.other.deepreview4offlinecomputing.SparkSQLUtils
//
//import java.sql.{Connection, DriverManager, PreparedStatement}
//
///**
//  * MySQL操作工具类
//  */
//object MySQLUtils {
//
//  /**
//    * 获取MySQL数据库链接
//    */
//  def getConnection(): Connection = {
//    DriverManager.getConnection("jdbc:mysql://localhost:3306/imooc_project?characterEncoding=UTF-8&user=root&password=root")
//  }
//
//  /**
//    * TODO...
//    *
//    * 释放JDBC连接等资源
//    */
//  def release(connection: Connection, pstmt: PreparedStatement): Unit = {
//    try {
//      if (pstmt != null) {
//        pstmt.close()
//      }
//    } catch {
//      case e: Exception => e.printStackTrace()
//    } finally {
//      if (connection != null) {
//        connection.close()
//      }
//    }
//  }
//
//  def main(args: Array[String]): Unit = {
//    println(getConnection())
//  }
//}
