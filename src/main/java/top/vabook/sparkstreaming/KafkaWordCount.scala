package top.vabook.sparkstreaming

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

/**
  * @author vabook@163.com
  * @version 2018/7/16 22:30
  *
  * 流程
  * 1.array(zk,group,topic,numthread) = args
  * 2.conf,ssc,topicMap
  * 3.createStream(ssc,zk,topicMap,StorageLevel)
  * 4.upstateByKey(func1,hashPartitioner,true)
  */
object KafkaWordCount {
  //upstateByKey函数的实现
  val updateFunc = (iter : Iterator[(String ,Seq[Int],Option[Int])]) => {
//   iter.flatMap(it => Some(it._2.sum + it._3.getOrElse(0)).map(x => (it._1,x)))
    iter.flatMap{case (x,y,z) => Some(y.sum + z.getOrElse(0)).map(v => (x,v))}
  }

  def main(args: Array[String]): Unit = {
    val Array(zkQuorum,group,topics,numThreads) = args
    val conf = new SparkConf().setAppName("KafkaCount")
    val sc = new SparkContext(sc)
    val ssc = new StreamingContext(sc,Seconds(5))
//    val ssc = SparkSession.builder().getOrCreate() //有问题
    val topicMap = topics.split(",").map((_,numThreads.toInt)).toMap
    //接收数据,参数为ssc,zk,group,topicMap,storageLevel
    val data = KafkaUtils.createStream(ssc,zkQuorum,group,topicMap,StorageLevel.MEMORY_AND_DISK_SER)
    val words = data.map(_._2).flatMap(_.split(" "))
    val wordCounts = words.map((_,1)).updateStateByKey(updateFunc,new HashPartitioner(sc.defaultParallelism),true)
    println(wordCounts.toString.toBuffer)
    ssc.start()
    ssc.awaitTermination()
  }

}
