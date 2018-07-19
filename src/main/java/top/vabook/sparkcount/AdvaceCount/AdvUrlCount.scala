package top.vabook.sparkcount.AdvaceCount

import java.net.URL

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author vabook@163.com
  * @version 2018/7/14 21:33
  *
  */
object AdvUrlCount {
  def main(args: Array[String]): Unit = {
    //将规则放入内存中
    val arr = Array("java","php","net")

    val conf = new SparkConf()
      .setAppName("AdvUrlCount")
      .setMaster("local")
    val sc = new SparkContext(conf)

    val rdd1 = sc.textFile("e://scala/log/...").map(line => {
      //对每一行数据进行切割
      val f = line.split("\t")
      //返回指定的字段,和count
      (f(1),1)
    })
    //rdd2 -> ("url...",200)
    val rdd2 = rdd1.reduceByKey(_ + _)

    //rdd3 -> (host,url,count)
    val rdd3 = rdd2.map( t => {
      val url = t._1
      //host ->java 从url中得到host
      val host = new URL(url).getHost
      //返回一个tuple
      (host,url,t._2)
    } )

    println(rdd3.collect().toBuffer)

    //对java学院进行排序
    val rddJava = rdd3.filter(_._1 == "java.itcast.cn")
    val sortedJava = rddJava.sortBy(_._3,false).take(3)

    //利用缓存中规则过滤
    for(ins <- arr){
      val rddArr = rdd3.filter(_._1 == ins)
      val result = rddArr.sortBy(_._3,false).take(3)
      println(result.toBuffer)
    }

    sc.stop()
  }
}
