package top.vabook.sparkcount

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author vabook@163.com
  * @version 2018/7/14 21:07
  *
  */
object WordCount {
  def main(args: Array[String]): Unit = {
    val config = new SparkConf().setAppName("Wordcount").setMaster("local[4]")
    val sc = new SparkContext(config)
    val rdd1 = sc.textFile(args(0))
    //默认保存在两个文件中
    val rdd2 = rdd1.flatMap(_.split(" ")).map((_,1)).reduceByKey(_ + _).sortBy(_._2,false).saveAsTextFile(args(1))
    sc.stop()

  }
}
