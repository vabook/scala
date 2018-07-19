package top.vabook.rpc

class WorkerInfo(val id : String, val memory : Int,val cores : Int) {
  //上一次的心跳
  var lastHeartbeatTime : Long = _
}
