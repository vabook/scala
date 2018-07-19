package top.vabook.rpc

import java.util.UUID

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._


class Worker (val masterHost : String ,val masterPort : Int, val memory : Int ,val cores: Int) extends Actor{
  var master : ActorSelection = _
  val workerId = UUID.randomUUID().toString
  val HEART_INTERVAL = 10000

  override def preStart(): Unit = {
    //跟master建立连接
    println("启动worker")
    master = context.actorSelection(s"akka.tcp://MasterSystem@$masterHost:$masterPort/user/Master")
    //刚连接上就注册,发送的是一个样例类
    master ! RegisterWorker(workerId,memory,cores)
  }
 
  override def receive: Receive = {
    //接收到回馈的注册信息,开始不断向自身发送心跳信息
    case RegisteredWorker(masterUrl) => {
      import context.dispatcher
      context.system.scheduler.schedule(0 millis, HEART_INTERVAL millis, self, SendHeartbeat)
    }
      //模拟master,自身接收自身的心跳
    case SendHeartbeat => {
      println("send heartbeat to master")
      //向master发送心跳信息
      master ! HeartBeat(workerId)
    }
  }
}

object Worker {
  def main(args: Array[String]): Unit = {
    val host = args(0)
    val port = args(1).toInt
    val masterHost = args(2)
    val masterPort = args(3).toInt
    val memory = args(4).toInt
    val cores = args(5).toInt

    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
       """.stripMargin
    val config = ConfigFactory.parseString(configStr)
    //根据配置文件创建actorSystem,并起一个别名
    val actorSystem = ActorSystem("WorkerSystem",config)
    //创建worker的时候并没有固定的常量接收,是变量
    actorSystem.actorOf(Props(new Worker(masterHost,masterPort,memory,cores)),"Worker")
    actorSystem.awaitTermination()
  }
}
