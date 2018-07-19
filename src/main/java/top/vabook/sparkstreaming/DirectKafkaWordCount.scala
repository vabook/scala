package top.vabook.sparkstreaming

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka._

/**
  * @author vabook@163.com
  * @version 2018/7/17 22:51
  *
  * 流程
  * 1.接收kafka的参数 brokers,topics,groupId-> set
  * 2.conf.set()
  * 3.kafkaParams = ..
  * 4.createDirectStream
  * 5.foreachRDD
  */
object DirectKafkaWordCount {
  def processRdd(rdd: RDD[(String,String)]): Unit = {
    //取出第二个元素
    val lines = rdd.map(_._2)
    val words = lines.map(_.split(" "))
    val wordCount = words.map(x => (x,1L)).reduceByKey(_ + _)
    wordCount.foreach(println)

  }

  def main(args: Array[String]): Unit = {
    if (args.length < 3){
      System.err.println(
        s"""
           |Usage: ....
         """.stripMargin
      )
      System.exit(1)
    }
    val Array(brokers,topics,groupId) = args

    val conf = new SparkConf().setAppName("KafkaWordcount").setMaster("local[4]")
    conf.set("spark.streaming.kafka.maxRatePerPartition", "5")
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val ssc = new StreamingContext(conf,Seconds(2))

    val topicSet = topics.split(",").toSet
    val kafkaParams = Map[String, String](
      "meta.broker.list" -> brokers,
      "group.id" -> groupId,
      "auto.offset.reset" -> "smallest"
    )
//    val km = new KafkaManager(kafkaParams)
    val message = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder ](ssc,kafkaParams,topicSet)
    message.foreachRDD(rdd => {
      processRdd(rdd)
      //设置zk偏移量
      //km.updateZKOffsets(rdd)
    })
    ssc.start()
    ssc.awaitTermination()
  }

}
