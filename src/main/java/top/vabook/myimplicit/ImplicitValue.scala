package top.vabook.myimplicit

//所有隐式值和隐式方法都必须放在object中
object Context{
  implicit val a = "yyy"
  implicit val i = 1
}
object ImplicitValue {
  //偏函数
  def sayHi()(implicit name : String = "xxx") : Unit = {
    println(s"$name")
  }

  def main(args: Array[String]): Unit = {
    println(1 to 10)
    sayHi()("xiaoming")
  }
}
