package top.vabook.sparkcount.AdvaceCount

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author vabook@163.com
  * @version 2018/7/14 22:09
  *
  */
object UserLocation {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName("UserLocation")
      .setMaster("local")
    val sc = new SparkContext(conf)

    //val rdd1 -> ("xx_xx", )
    val rdd1 = sc.textFile("e://scala/bs_log").map(line => {
      val fields = line.split(",")
      val eventType = fields(3)
      val time = fields(1)
      val timeLong = if(eventType == "1")  -time.toLong else time.toLong
      (fields(0) + "_" + fields(2),timeLong)
    })

    //groupBy ->("xx_xx",List((rdd1),(rdd1),(rdd1)...)),rdd2 -> ("xx_xx",count)
    val rdd2 = rdd1.groupBy(_._1).mapValues(_.foldLeft(0L)(_ + _._2))

    val rdd3 = rdd2.map(t => {
      val mobile_bs = t._1
      val mobile = mobile_bs.split("_")(0)
      val lac = mobile_bs.split("_")(1)
      val time = t._2
      (mobile,lac,time)
    })

    //rdd4 -> (mobile,List((rdd3),(rdd3),()...))
    val rdd4 = rdd3.groupBy(_._1)
    val rdd5 = rdd4.mapValues(it => {
      it.toList.sortBy(_._3).reverse.take(3)
    })
    println(rdd5.collect().toBuffer)
    sc.stop()
  }

}
