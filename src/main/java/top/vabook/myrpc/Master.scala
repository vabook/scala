package top.vabook.myrpc

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory


class Master extends Actor {
  println("constructor invoked")

  override def preStart(): Unit = {
    println("preStart invoked")
  }
  //接收消息
  override def receive: Receive = {
    case "connect" => {
      println("a client connected")
      sender ! "reply"
    }
    case "hello" => {
      println("hello")
    }
  }
}
object Master {
  def main(args: Array[String]): Unit = {
    val host = args(0)
    val port = args(1).toInt

    val configStr =
      s"""
        |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
        |akka.remote.netty.tcp.hostname = "$host"
        |akka.remote.netty.tcp.port = "$port"
      """.stripMargin

    val config = ConfigFactory.parseString(configStr)

    //ActorSystem 单例,辅助创建和监控actor
    val actorSystem = ActorSystem("MasterSystem",config)

    //创建actor ,起名字,会执行Master的主构造器
    val master = actorSystem.actorOf(Props[Master],"Master")

    master ! "hello"
    //进程等待
    actorSystem.awaitTermination()
  }
}
