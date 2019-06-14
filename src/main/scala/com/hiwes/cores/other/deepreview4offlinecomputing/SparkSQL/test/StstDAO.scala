//package com.hiwes.cores.other.deepreview4offlinecomputing.SparkSQL.test
//
//import java.sql.{Connection, PreparedStatement}
//
//import com.hiwes.cores.other.deepreview4offlinecomputing.SparkSQLUtils.MySQLUtils
//
//import scala.collection.mutable.ListBuffer
//
///**
//  * 统计各个维度的DAO操作
//  */
//object StatDAO {
//
//  /**
//    * 批量保存DayVideoAccessStat到数据库
//    */
//  def insertDayVideoAccessTopN(list: ListBuffer[DayVideoAccessStat]) = {
//    var connection: Connection = null
//    var pstmt: PreparedStatement = null
//
//    try {
//      connection = MySQLUtils.getConnection()
//
//      connection.setAutoCommit(false) // 设置手动提交
//
//
//      val sql = "insert into day_video_access_topn_stat(day,cmsId,times) values (?,?,?)"
//      pstmt = connection.prepareStatement(sql)
//
//      for (ele <- list) {
//        pstmt.setString(1, ele.day)
//        pstmt.setLong(2, ele.cmsId)
//        pstmt.setLong(3, ele.times)
//        pstmt.addBatch()
//      }
//      pstmt.executeBatch()  // 执行批量处理
//      connection.commit()   // 手动提交
//    } catch {
//      case e: Exception => e.printStackTrace()
//    } finally {
//      MySQLUtils.release(connection, pstmt)
//    }
//  }
//}
