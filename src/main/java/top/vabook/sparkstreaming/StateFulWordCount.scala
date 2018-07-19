package top.vabook.sparkstreaming

import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}
import org.apache.spark.sparkstreaming.LoggerLevels
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * @author vabook@163.com
  * @version 2018/7/16 17:23
  *          upstateByKey 需要设置保存点,会自动进行保存,并创建一个函数,参数为一个迭代器
  */
class StateFulWordCount {
  val updateFunc = (iter: Iterator[(String, Seq[Int], Option[Int])]) => {
    iter.flatMap(it => Some(it._2.sum + it._3.getOrElse(0)).map(x => (it._1, x)))

    //    iter.map{
    //      case (x,y,z) => Some(y.sum + z.getOrElse(0)).map(m => (x,m))
    //    }
    //    iter.map(t => (t._1,t._2.sum + t._3.getOrElse(0)))
    def main(args: Array[String]): Unit = {
      LoggerLevels
      val conf = new SparkConf().setAppName("StateFulWordCount").setMaster("local[2]")
      val sc = new SparkContext(conf)
      sc.setCheckpointDir("e://scala/checkDir")
      val ssc = new StreamingContext(sc, Seconds(5))
      val ds = ssc.socketTextStream("localhost", 8888)
      val result = ds.flatMap(_.split(" ")).map((_, 1)).updateStateByKey(updateFunc, new HashPartitioner(sc.defaultParallelism), true)
      result.print()
      ssc.start()
      ssc.awaitTermination()
    }
  }
}
