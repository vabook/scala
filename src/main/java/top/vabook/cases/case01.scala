package top.vabook.cases

import scala.util.Random

/**
  * case 根据内容判断
  */
object case01 extends App {

  val arr = Array[String]("hello world", "hello tom", "hello jerry")
  val sentence = arr(Random.nextInt(arr.length))
  println(sentence)
  sentence match {
    case "hello world" => println(sentence + "第一条语句")
    case "hello tom" => println(sentence + "第二条语句")
    case _ => println("no one")
  }

}
