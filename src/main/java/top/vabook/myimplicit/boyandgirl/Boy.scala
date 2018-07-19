package top.vabook.myimplicit.boyandgirl

/**
  * @author vabook@163.com
  * @version 2018/7/13 15:53
  *
  */
class Boy(val name : String, val faceValue : Int) extends Comparable[Boy] {
  override def compareTo(o: Boy): Int = {
    this.faceValue - o.faceValue
  }
}
