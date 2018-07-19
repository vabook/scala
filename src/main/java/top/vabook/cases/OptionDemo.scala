package top.vabook.cases


/**
  * 可选择 匹配some 或者none
  */
object OptionDemo {
  def main(args: Array[String]): Unit = {
    val map = Map("a" -> 1, "b" -> 2)
    val v = map.get("b") match {
      case Some(i) => i
      case None => 0
    }
    println(v)

    val v1 = map.getOrElse("x", 0)
    println(v1)
  }

}