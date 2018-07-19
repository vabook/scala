package top.vabook.cases

import scala.util.Random

/**
  * 样例类
  */
case class SubmitTask(id : String, name : String)
case class HeartBeat(time : Long)

//单例对象可以没有参数
case object CheckTimeOutTask

object case04 extends App {
  val arr = Array(CheckTimeOutTask, HeartBeat(123), SubmitTask("001", "task-001"))

  val a = CheckTimeOutTask

  val b = CheckTimeOutTask

  //样例类在模式匹配中,和声明中都需要设置参数
  arr(Random.nextInt(arr.length)) match {

    case SubmitTask(id, name) => {
      println(s"$id $name")
    }
    case HeartBeat(time) => {
      println(time)
    }
    case CheckTimeOutTask => {
      println("check")
    }
  }


}

