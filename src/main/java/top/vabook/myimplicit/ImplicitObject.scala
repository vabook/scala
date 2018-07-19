package top.vabook.myimplicit

/**
  * @author vabook@163.com
  * @version 2018/7/13 15:22
  *
  */
object ImplicitObject extends App {

  //接口,未实现的方法
  trait Multiplicable[T] {
    def multiply(x: T): T
  }
  //隐式对象继承接口,并重写内部的方法
  implicit object MultiplicableInt extends Multiplicable[Int] {
    override def multiply(x: Int): Int = x * x
  }

  implicit object MultiplicableString extends Multiplicable[String] {
    override def multiply(x: String): String = x * 2
  }
  /*//定义函数,泛型为接口类型,参数,返回值类型
  def multiply[T : Multiplicable](x : T) : T = {
    //用于访问隐式对象
    val reslut = implicitly[Multiplicable[T]]
    //调用方法,会进行隐式转化
    reslut.multiply(x)
  }*/

  //隐式参数
  def multiply[T : Multiplicable](x : T)(implicit result : Multiplicable[T]) : T = {
    result.multiply(x)
  }
  println(multiply(5))

  println(multiply("sss"))

}
