package top.vabook.myimplicit.boyandgirl

/**
  * @author vabook@163.com
  * @version 2018/7/13 16:47
  *
  */
class MissRight[T] {
  //隐式函数作为参数
  def choose(first: T, second: T)(implicit ord: T => Ordered[T]): T = {
    if (first > second) first else second
  }

  //隐式数值作为参数
  def select(first: T, second: T)(implicit ord: Ordering[T]): T = {
    if (ord.gt(first, second)) first else second
  }

  def random(first: T, second: T)(implicit ord: Ordering[T]): T = {
    import Ordered.orderingToOrdered
    if (first > second) first else second
  }
}

object MissRight {
  def main(args: Array[String]): Unit = {
    import top.vabook.myimplicit.MyPredef.girlOrdering
    val mr = new MissRight[Girl]
    val g1 = new Girl("ht",99,28)
    val g2 = new Girl("sora",99,22)
    val g3 = mr.choose(g1,g2)
    println(g2.name)
  }
}
