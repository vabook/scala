package top.vabook.rpc

trait RemoteMessage extends Serializable{

}

//worker -> master
case class RegisterWorker(id : String , memory : Int ,cores : Int) extends RemoteMessage

//worker -> master
case class HeartBeat(id : String ) extends RemoteMessage

//Master -> worker
case class RegisteredWorker(masterUrl : String) extends RemoteMessage

//worker -> self
case object SendHeartbeat

//master -> self
case object CheckTimeOutWorker