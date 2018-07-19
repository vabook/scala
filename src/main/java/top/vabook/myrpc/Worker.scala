package top.vabook.myrpc

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

class Worker(val masterHost : String, val masterPort : Int) extends Actor {

  var master: ActorSelection = _

  override def preStart(): Unit = {

    //worker启动时会打印下面的协议,判断连接到哪个master
    master = context.actorSelection(s"akka.tcp://MasterSystem@$masterHost:$masterPort/user/Master")
    //
    master ! "connect"
  }

  override def receive: Receive ={
    case "reply" => {
      println("a reply from master")
    }
  }
}
object Worker {
  def main(args: Array[String]): Unit = {
    val host = args(0)
    val port = args(1).toInt

    val masterHost = args(2)
    val masterPort = args(3).toInt

    val configStr =
      s"""
        |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
        |akka.remote.netty.tcp.hostname = "$host"
        |akka.remote.netty.tcp.port = "$port"
      """.stripMargin
    val config = ConfigFactory.parseString(configStr)

    val actorSystem = ActorSystem("WorkerSystem",config)
    actorSystem.actorOf(Props(new Worker(masterHost,masterPort)),"Worker")
    actorSystem.awaitTermination()
  }
}
