package top.vabook.operator

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author vabook@163.com
  * @version 2018/7/13 19:50
  *
  */
object BasicOperator {
  def main(args: Array[String]): Unit = {

    val config = new SparkConf()
    config.setAppName("mycount")
    config.setMaster("local")
    val sc = new SparkContext(config)

    val rdd1 = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 2)

    //将每一个分区中的元素变成List,然后对每一个元素进行map操作
    val func = (index: Int, iter: Iterator[(Int)]) => {
      iter.toList.map(x => "[partID:" + index + ", val: " + x + ']').iterator
    }

    val rdd2 = rdd1.mapPartitionsWithIndex(func)
    val res = rdd2.collect()
    //    res.foreach(println)

    //------------aggregate-------------
    //-------
    //----
    def fun1(index: Int, iter: Iterator[(Int)]): Iterator[String] = {
      iter.toList.map(x => "[part : " + index + "val : " + x + "]").iterator
    }

    val rdd3 = sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 2)
    rdd3.mapPartitionsWithIndex(fun1)
    val rdd4 = rdd3.aggregate(0)(_ + _, _ + _)
    //    println(rdd4)
    val rdd5 = rdd3.aggregate(10)(_ + _, _ + _)
    //    println(rdd5)
    //    println(rdd3.aggregate(0)(math.max(_,_),_ + _))
    //    println(rdd3.aggregate(10)(math.max(_,_),_ + _))
    //    println(rdd3.aggregate(5)(math.max(_,_),_ + _))

    val rdd6 = sc.parallelize(List("a", "b", "c", "d", "e", "f", "g", "h"), 2)

    def fun2(index: Int, iter: Iterator[(String)]): Iterator[String] = {
      iter.toList.map(x => "[Part : " + index + " val : " + x + "]").iterator
    }

    val rdd7 = rdd6.mapPartitionsWithIndex(fun2)
    //    rdd7.foreach(println)
    //      首先必须加上"" ,其次,必须toString,不加就不能跟后面的比较了
    //    println(rdd6.aggregate("")((x,y) => math.max(x.length,y.length).toString,(x,y) => x + y))
    //    println(rdd6.aggregate("")(_ + _ , _ + _))
    //    println(rdd6.aggregate("=")(_ + _, _ + _))

    val rdd8 = sc.parallelize(List("12", "23", "345", "5678"), 2)
    val rdd9 = rdd8.mapPartitionsWithIndex(fun2)
    //    rdd9.foreach(println)
    //    println(rdd8.aggregate("")((x,y) => math.max(x.length,y.length).toString,(x,y) => x + y))


    //*************aggregate By Key***************

    val pariRDD = sc.parallelize(List(("dog", 3), ("snake", 6), ("snake", 3), ("mouse", 4), ("dog", 2), ("cat", 3)), 2)

    def fun3(index: Int, iter: Iterator[(String, Int)]): Iterator[String] = {
      iter.toList.map(x => "[Part : " + index + ", val : " + x + "]").iterator
    }

    val rdd10 = pariRDD.mapPartitionsWithIndex(fun3)
    //    rdd10.collect()
    //    rdd10.foreach(println)
    //    println(pariRDD.aggregateByKey(0)(math.max(_ , _ ),_ + _).collect().toBuffer)
    //    println(pariRDD.aggregateByKey(100)(math.max(_ , _ ),_ + _).collect().toBuffer)

    sc.setCheckpointDir("e:/scala")

    val rdd11 = sc.parallelize(1 to 10, 4)
    //    println(rdd11.partitions.length)
    val rdd12 = rdd11.coalesce(4)
    //    println(rdd12.partitions.length)

    val rdd13 = sc.parallelize(List(("a", 1), ("b", 2)))
    val rdd14 = rdd13.collectAsMap()
    //    rdd14.foreach(x => println(x._1 + " " + x._2))

    //************conbineByKey***************
    //******
    //***

    val rdd15 = sc.textFile("e:/scala/hello.txt").flatMap(_.split(" ")).map((_, 1))
    val rdd16 = rdd15.combineByKey(x => x, (a: Int, b: Int) => a + b, (m: Int, n: Int) => m + n)
    //        println(rdd16.collect().toBuffer)
    //10是每个key的起始值, 而不是对key中的每个元素加10
    val rdd17 = rdd15.combineByKey(x => x + 10, (a: Int, b: Int) => a + b, (m: Int, n: Int) => m + n)
    //        println(rdd17.collect().toBuffer)

    val rdd18 = sc.parallelize(List("dog", "cat", "snake", "mouse", "bee"), 3)
    //    val rdd19 = rdd18.mapPartitionsWithIndex(fun2).foreach(println)

    val rdd20 = sc.parallelize(List(2, 3, 3, 1, 5), 3)

    val rdd21 = rdd20.zip(rdd18)
    //    rdd21.foreach(println)
    val rdd22 = rdd21.combineByKey(List(_), (x: List[String], y: String) => x :+ y, (m: List[String], n: List[String]) => m ++ n)
    //   rdd22.collect().toBuffer.foreach(println)

    //  ******countByKey*******
    //list里面是tuple
    val rdd23 = sc.parallelize(List(("a", 1), ("b", 2), ("c", 3), ("e", 4), ("a", 1), ("b", 2)), 3)
    //    println(rdd23.countByKey())
    //跟想象中的略有不同
    //    println(rdd23.countByValue())

    //*****filterByRange********
    val rdd24 = sc.parallelize(List(("e", 5), ("c", 3), ("d", 4), ("c", 2), ("a", 1)))
    val rdd25 = rdd24.filterByRange("b", "c")
    //    rdd25.foreach(println)

    val rdd26 = sc.parallelize(List(("a", "1 2"), ("b", "3 4"), ("c", "5 6"), ("d", "7 8")), 2)
    val rdd27 = rdd26.flatMapValues(_.split(" "))
    val rdd28 = rdd27.collect().toBuffer
    //    println(rdd28)

    val rdd29 = sc.parallelize(List(12, 3, 4, 5, 6, 7), 3)

    val rdd30 = rdd29.foreachPartition(x => println(x.reduce(_ + _))).formatted("")
    //如果不format不能进行foreach
    //    rdd30.foreach(println)
    //    println(rdd30)

    //**********foldByKey***************
    val rdd31 = sc.parallelize(List("dog", "wolf", "cat", "bear"), 2)
    val rdd32 = rdd31.map(x => (x.length, x))
    //    rdd32.foreach(println)
    val rdd33 = rdd32.foldByKey("")(_ + _)
    //    rdd33.foreach(println)

    //keyBy ,以传入的参数作为key
    val rdd34 = sc.parallelize(List("dog", "salmon", "salmon", "rat", "elephant"), 3)
    val rdd35 = rdd34.keyBy(_.length)
    rdd35.foreach(println)


    val rdd36 = sc.parallelize(List("dog", "tiger", "lion", "cat", "panther", "eagle"), 2)
    val rdd37 = rdd36.map(x => (x.length,x))
    val rdd38 = rdd37.keys.collect()
    rdd38.foreach(println)
    val rdd39 = rdd37.values.collect()
    rdd39.foreach(println)
  }
}
