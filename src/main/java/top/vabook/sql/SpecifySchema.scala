package top.vabook.sql

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author vabook@163.com
  * @version 2018/7/15 22:39
  * 流程
  * 1.创建源Rdd
  * 2.创建schema -> structType 结构类型
  * 3.将源Rdd转换为RowRdd
  * 4.创建数据框,参数为RowRdd,schema
  * 5.创建视图,createor....
  * 6.通过sqlContext执行sql语句
  */
object SpecifySchema {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Sql-schema")
    val sc = new SparkContext(conf)
//    val sqlContext = new SQLContext(sc)
    val sqlContext = SparkSession.builder().getOrCreate()

    //rdd1 -> ((1,tom,12),()...)
    val rdd1 = sc.textFile(args(0)).map(_.split(" "))
    val schema = StructType(
      List(
        StructField("id", IntegerType, true),
        StructField("name", StringType, true),
        StructField("age", IntegerType, true)
      )
    )
    //将rdd1映射到RowRDD
    val rowRdd = rdd1.map(p => Row(p(0).toInt,p(1).toString,p(2).toInt))

    //创建数据框,应用RowRdd 和 结构类型
    val personDataFrame = sqlContext.createDataFrame(rowRdd,schema)

    personDataFrame.createOrReplaceTempView("t_person")

    val df = sqlContext.sql("select * from t_person order by age desc limit 2")

    df.write.json(args(1))
    sc.stop()

  }
}

