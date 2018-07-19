package top.vabook.cases

import scala.util.Random

/**
  * case 根据类型判断
  */
object case02 extends App {

  val arr = Array("hello", 1, -2.0, case02)
  var elem = arr(Random.nextInt(arr.length))
  elem = arr(2)
  println(elem)

  elem match {
    case x : Int => println(x)

      //怎么回事,竟然还返回-2,0
    case y : Double => print(if (y > 0) y else -y)

    case z : String => println(z)

    case i : App => println(i.getClass)

    case _ => throw new Exception("not match")
  }

}
