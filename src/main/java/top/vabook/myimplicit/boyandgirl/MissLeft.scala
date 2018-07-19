package top.vabook.myimplicit.boyandgirl

/**
  * @author vabook@163.com
  * @version 2018/7/13 16:47
  *
  */
class MissLeft[T : Ordering] {
  def choose(first : T, second : T): T = {
    val ord = implicitly[Ordering[T]]
    if (ord.gt(first,second)) first else second
  }
}
object MissLeft{
  def main(args: Array[String]): Unit = {
    import top.vabook.myimplicit.MyPredef.girlOrdering
    val m1 = new MissLeft[Girl]
    val g1 = new Girl("hh",98,28)
    val g2 = new Girl("sora",97,33)
    val g = m1.choose(g1,g2)
    println(g.name)
  }
}