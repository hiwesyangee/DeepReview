//package com.hiwes.cores.other.deepreview4offlinecomputing.SparkSQLUtils
//
//import org.apache.spark.sql.Row
//import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}
//
//object AccessConvertUtil {
//  val struct = StructType(
//    Array(
//      StructField("url", StringType),
//      StructField("cmsType", StringType),
//      StructField("cmsId", LongType),
//      StructField("traffic", LongType),
//      StructField("ip", StringType),
//      StructField("city", StringType),
//      StructField("time", StringType),
//      StructField("day", StringType)
//    )
//  )
//
//  def parseLog(log: String): Row = {
//    try {
//      val splits: Array[String] = log.split("\t")
//      val times: String = splits(0)
//      val day: String = times.substring(0, 10).replaceAll("-", "") // 当天
//      //
//      val url: String = splits(1) // 访问url——String
//
//      val domain = "http://www.imooc.com/"
//      val cms = url.substring(url.indexOf(domain) + domain.length)
//      val cmsTypeId = cms.split("/")
//
//      var cmsType: String = ""
//      var cmsId: Long = 0l
//      if (cmsTypeId.length > 1) {
//        cmsType = cmsTypeId(0) // 课程类型
//        cmsId = cmsTypeId(1).toLong // 课程ID
//      }
//
//      val traffic: Long = splits(2).toLong // 消耗流量——Long
//      val ip: String = splits(3) // ip——String
//      val city: String = IpUtils.getCity(ip)
//
//      // String,String,Long,Long,String,String,String,String
//      //        url, cmsType, cmsId, traffic, ip, city, times, day
//      Row(url, cmsType, cmsId, traffic, ip, city, times, day)
//    } catch {
//      case e => Row(0)
//    }
//  }
//}
