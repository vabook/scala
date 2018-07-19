package top.vabook.sparkcount


import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author vabook@163.com
  * @version 2018/7/14 21:18
  * 用于远程debug,需要在配置参数中指定远程的文件输入输出位置
  * 指定打包位置,本地打包
  *
  */
object WordCount2 {
  def main(args: Array[String]): Unit = {

    val jarPath = ""
    val conf = new SparkConf()
      .setMaster("spark://Centos-01:7077")
      .setJars(Array(""))
      .setAppName("WordCountOnCluster")
    val sc = new SparkContext(conf)

    //产生两个RDD:一个是HadoopRDD(读取) MapPartitionRDD(存储RDD)
    sc.textFile(args(1))
      //读取文件的时候直接缓存起来
      .cache()
      //MapPartitionRDD
      .flatMap(_.split(" "))
      //MapPartitionRDD
      .map((_, 1))
      //产生ShuffleRDD
      .reduceByKey(_ + _)
      //产生MapPartitionRDD
      .saveAsTextFile(args(1))

    sc.stop()


  }
}
