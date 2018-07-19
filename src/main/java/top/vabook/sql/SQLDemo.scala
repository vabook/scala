package top.vabook.sql

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author vabook@163.com
  * @version 2018/7/16 16:01
  * 利用样例类构建表结构
  * 参数为输入文件目录(hdfs文件目录),输出文件目录(同上)
  * 流程
  * 1.personRdd -> person(, , )
  * 2.import df...
  * 3.createorrepl....
  * 4.sql查询
  * 5.show 或者 write
  */
object SQLDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("SqlDemo")
    val sc = new SparkContext(conf)
    //    val sqlContext = new SQLContext(sc)
    val sqlContext = SparkSession.builder().getOrCreate()
    val personRdd = sc.textFile(args(0)).map(line => {
      val fields = line.split(",")
      Person(fields(0).toInt,fields(1),fields(2).toInt)
    })

    import sqlContext.implicits._
    val df = personRdd.toDF

    df.createOrReplaceTempView("t_person")

    val result = sqlContext.sql("select * from t_person where age > 20 order by age desc limit 2")

    result.show()
    result.write.json("args(1)")
    sc.stop()
  }
}

case class Person(id: Int, name: String, age: Int)
