package com.hiwes.cores.other.deepreview4offlinecomputing.SparkSQL.test

import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale
;

/**
  * 日期时间解析工具类
  */
object DataUtils {
  // 输入文件原有日期格式
  // [10/Nov/2016:00:01:02 +0800]   使用FastDateFormat，线程安全，而不用SimpleDateFormat
  val YYYMMDDHHMM_TIME_FORMAT = new SimpleDateFormat("dd/MMM/yy:HH:mm:ss Z", Locale.ENGLISH)
  // 输出文件目标日期格式
  val TARGET_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  /**
    * 获取时间    yyyy-MM-dd HH:mm:ss
    * @param time
    */
  def parse(time:String) = {
      TARGET_FORMAT.format(new Date(getTime(time)))
  }

  /**
    * 获取输入日志时间
    * @param time:[10/Nov/2016:00:01:02 +0800]
    * @return
    */
  def getTime(time:String) = {
    try {
      YYYMMDDHHMM_TIME_FORMAT.parse(time.substring(time.indexOf("[") + 1, time.lastIndexOf("]"))).getTime
    }catch{
      case e:Exception => {
        0l
      }
    }
  }

  def main(args: Array[String]): Unit = {
    println(parse("[10/Nov/2016:00:01:02 +0800]"))
  }
}
