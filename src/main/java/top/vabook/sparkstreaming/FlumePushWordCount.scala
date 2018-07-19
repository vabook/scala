package top.vabook.sparkstreaming

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.flume.FlumeUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * @author vabook@163.com
  * @version 2018/7/16 23:05
  *
  * 流程
  * 1.host port
  * 2.createStream
  * 3.逻辑操作,flatMap 对一行数据操作时,event.getBody.array.split.toString
  */
object FlumePushWordCount {
  def main(args: Array[String]): Unit = {
    val host = args(0)
    val port = args(1).toInt
    val conf = new SparkConf().setAppName("FlumePush").setMaster("local[4]")
    val ssc = new StreamingContext(conf,Seconds(5))

    val flumeStream = FlumeUtils.createStream(ssc,host,port,StorageLevel.MEMORY_AND_DISK)
    val words = flumeStream.flatMap(x => new String(x.event.getBody().array()).split(" ")).map((_,1))

    val result = words.reduceByKey(_ + _)
    result.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
