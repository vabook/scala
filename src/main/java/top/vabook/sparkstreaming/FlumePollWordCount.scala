package top.vabook.sparkstreaming

import java.net.InetSocketAddress

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.flume.FlumeUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * @author vabook@163.com
  * @version 2018/7/17 22:43
  *
  * 流程
  * 1.address
  * 2.createPollingStream
  * 3.逻辑操作
  */
class FlumePollWordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("FlumePollWordCount").setMaster("local[2]")
    val ssc = new StreamingContext(conf, Seconds(5))

    val address = Seq(new InetSocketAddress("Centos-01",8888))
    val flumeStream = FlumeUtils.createPollingStream(ssc,address,StorageLevel.MEMORY_AND_DISK)

    val words = flumeStream.flatMap(x => new String(x.event.getBody.array()).split(" ")).map((_,1))

    val result = words.reduceByKey(_ + _)
    result.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
