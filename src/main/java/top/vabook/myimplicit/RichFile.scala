package top.vabook.myimplicit
import java.io.File

import scala.io.Source

class RichFile(val f : File) {
  def read() = Source.fromFile(f).mkString
}
object RichFile{
  def main(args: Array[String]): Unit = {
    val f = new File("e://scala/hello.txt")
    //装饰显示增强,使用这个的话就不能使用隐式转换了
//    val contexts = new RichFile(f).read()
    import MyPredef.fileToRichfile
    val context = f.read()
    println(context)
  }
}
