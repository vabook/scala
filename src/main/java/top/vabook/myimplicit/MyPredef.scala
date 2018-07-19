package top.vabook.myimplicit

import java.io.File

import top.vabook.myimplicit.boyandgirl.Girl

object MyPredef {
  implicit def fileToRichfile(f : File) = new RichFile(f)

  //隐式方法
  implicit def girlToOrdered(girl : Girl) = new Ordered[Girl]{
    override def compare(that: Girl): Int = {
      if (girl.faceValue == that.faceValue){
        girl.size - that.size
      }else{
        girl.faceValue - that.faceValue
      }
    }
  }
  //隐式对象
  implicit object girlOrdering extends Ordering[Girl]{
    override def compare(x: Girl, y: Girl): Int = {
      if(x.faceValue == y.faceValue){
        x.size - y.size
      }else{
        x.faceValue - y.faceValue
      }
    }
  }
}
