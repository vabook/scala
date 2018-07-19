package top.vabook.sparkcount.AdvaceCount

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author vabook@163.com
  * @version 2018/7/14 21:52
  *
  */
object AdvUserLocation {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local")
      .setAppName("AdvUserLocation")
    val sc = new SparkContext(conf)

    //rdd0 -> ((,),timeLong)
    val rdd0 = sc.textFile("e://scala/bs_log").map(line => {
      val fields = line.split(",")
      val eventType = fields(3)
      val time = fields(1)
      val timeLong = if (eventType == "1") -time.toLong else time.toLong
      ((fields(0),fields(2)),timeLong)
    })

    //rdd1 -> (, (, ))
    val rdd1 = rdd0.reduceByKey(_ + _).map(t => {
      val mobile = t._1._1
      val lac = t._1._2
      val time = t._2
      (lac,(mobile,time))
    })
    //rdd2 -> (, , )
    val rdd2 = sc.textFile("e://scala/lac_info.log").map(line => {
      val f = line.split(",")
      (f(0),(f(1),f(2)))
    })

    //rdd3 -> (, , , , )
    val rdd3 = rdd1.join(rdd2).map(t =>{
      val lac = t._1
      val mobile = t._2._1._1
      val time = t._2._1._2
      val x = t._2._2._1
      val y = t._2._2._2
      (mobile,lac,time,x,y)
    })

    val rdd4 = rdd3.groupBy(_._1)
    val rdd5 = rdd4.mapValues(it => {
      it.toList.sortBy(_._3).reverse.take(2).iterator
    })
    println(rdd5.collect().toBuffer)
    rdd5.saveAsTextFile("e://scala/hello/advUserLocation")
    sc.stop()
  }
}
