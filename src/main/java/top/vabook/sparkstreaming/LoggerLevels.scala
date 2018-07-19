package org.apache.spark.sparkstreaming
import org.apache.log4j.{Level, Logger}
import org.apache.spark.internal.Logging
/**
  * @author vabook@163.com
  * @version 2018/7/16 16:30
  *
  */
object LoggerLevels extends Logging{
  def log4jInitialized = Logger.getRootLogger.getAllAppenders.hasMoreElements
  if (!log4jInitialized){
    val logInfo = ("Setting log level to [WARN] for streaming example." +
      " To override add a custom log4j.properties to the classpath.")
    Logger.getRootLogger.setLevel(Level.WARN)
  }
}
