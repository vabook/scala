package top.vabook.sparkstreaming

import org.apache.spark.{HashPartitioner, SparkConf}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * @author vabook@163.com
  * @version 2018/7/17 22:11
  *
  * 流程
  * 1.createStream 从kafka中读取数据 并取出第二个元素
  * 2.upstateByKey  必须设置检查目录
  * 3.逻辑操作
  */
object UrlCount {
  val func = (iter: Iterator[(String, Seq[Int], Option[Int])]) => {
    iter.flatMap(it => Some(it._2.sum + it._3.getOrElse(0)).map(x => (it._1, x)))
    iter.flatMap { case (x, y, z) => Some(y.sum + z.getOrElse(0)).map(v => (x, v)) }
    iter.map(t => (t._1, t._2.sum + t._3.getOrElse(0)))
  }

  def main(args: Array[String]): Unit = {
    val Array(zkQuourm,groupId,topics,numThreads,hdfs) = args
    val conf = new SparkConf().setAppName("UrlWordCount").setMaster("local[4]")
    val ssc = new StreamingContext(conf,Seconds(5))
    ssc.checkpoint(hdfs)
    val topicMap = topics.split(",").map((_,numThreads.toInt)).toMap

    val line = KafkaUtils.createStream(ssc,zkQuourm,groupId,topicMap,StorageLevel.MEMORY_AND_DISK).map(_._2)

    val url = line.map(x => (x.split(" ")(6),1))
    val result = url.updateStateByKey(func,new HashPartitioner(ssc.sparkContext.defaultParallelism),true)

    result.print()
    ssc.start()
    ssc.awaitTermination()
  }

}
