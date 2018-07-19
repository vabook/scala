package top.vabook.sparkstreaming

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author vabook@163.com
  * @version 2018/7/16 17:00
  *
  */
object WindowsOpt {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("WindowsOpt")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(conf,Seconds(3))

    val lines = ssc.socketTextStream("localhost",8888)
    val pairs = lines.flatMap(_.split(" ")).map((_,1))

    val windowsWordCountss = pairs.reduceByKeyAndWindow((a:Int,b:Int) => (a + b),Seconds(15),Seconds(10))

    windowsWordCountss.print()


  }

}
