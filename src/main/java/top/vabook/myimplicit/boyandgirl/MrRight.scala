package top.vabook.myimplicit.boyandgirl

/**
  * @author vabook@163.com
  * @version 2018/7/13 16:47
  *
  */
class MrRight[T] {
  def choose[T <: Comparable[T]](first: T, second: T): T = {
    if (first.compareTo(second) > 0) first else second
  }
}

object MrRight {
  def main(args: Array[String]): Unit = {
    val mr = new MrRight[Boy]
    val b1 =new Boy("xiaoming",99)
    val b2 = new Boy("xiaogang",88)
    val b3 = mr.choose(b1,b2)
    println(b3.name)
  }
}