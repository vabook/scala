package top.vabook.cases

/**
  * 偏函数 定义函数的时候指明为偏函数类型,参数为 输入的类型,输出的类型
  */
object PartialFunctionDemo {
  def func1: PartialFunction[String, Int] = {
    case "one" => {
      println("one case")
      1
    }
    case "two" => 2
    case _ => -1
  }
  def func2(num : String) : Int = num match{
    case "one" => 1
    case "two" => 2
    case _ => -1
  }

  def main(args: Array[String]): Unit = {
    println(func1("two"))
    println(func2("one"))
  }
}
