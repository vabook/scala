package top.vabook.cases

import scala.util.Random

/**
  * 中级模式匹配
  */
object case03 extends App {
  var arr = Array(1,2,3,4)

  arr match  {
    case Array(1,x,y) => print(x + " " + y)
    case Array(1,2,3,4) => println("123")
    case Array(1,_*) => println("1.........")
    case _ => println("nothing")
  }

  val lst = List(0,5)
  lst match {
    case 0 :: Nil => println("only 0 ")
    case x :: y :: Nil => println(s"$x  $y ")
    case 0 :: a => println(s"0 ...$a")
    case _ => println("nothing")
  }

  val tup = (1,7,8)
  tup match{
    case (1,x,y) => println(s"hello $x $y")
    case (_,7,8) => println(s"_78")
    case _ => println("nothing")
  }

  val lst1 = 9 :: (5 :: (2 :: Nil))
  val lst2 = 9 :: 5 :: 2 :: None :: List()
  println(lst1)
  println(lst2)
}
