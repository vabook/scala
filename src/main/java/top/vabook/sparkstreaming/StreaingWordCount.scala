package top.vabook.sparkstreaming

import org.apache.spark.sparkstreaming.LoggerLevels
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author vabook@163.com
  * @version 2018/7/16 16:22
  *
  */
object StreaingWordCount {
  def main(args: Array[String]): Unit = {
    LoggerLevels.log4jInitialized
    val conf = new SparkConf().setAppName("StreamingWordCount").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc,Seconds(5))

    //监听端口
    val dStream = ssc.socketTextStream("localhost",8888)
    val result = dStream.flatMap(_.split(" ")).map((_,1)).reduceByKey(_ + _)
    result.print()

    //在这里并没有打印出来
    result.foreachRDD((x,y) => {
      println("word = " + x ,"value = " + y)
    })
    ssc.start()
    ssc.awaitTermination()
  }
}
