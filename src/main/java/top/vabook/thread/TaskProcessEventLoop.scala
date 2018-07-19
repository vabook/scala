package top.vabook.thread

/**
  * @author vabook@163.com
  * @version 2018/7/12 15:50
  *
  */
class TaskProcessEventLoop(name : String) extends EventLoop[TaskEvent](name){
  protected def onReceive(event : TaskEvent): Unit = event match{
    case TaskSubmitted(name) => {
      println(name + "submitted")
    }
    case TaskSucceeded(name) => {
      println(name + "succeeded")
    }
    case TaskFailed(name) => {
      println(name + "failed")
    }
  }
  override protected def onReceive : PartialFunction[TaskEvent,Unit] ={
    case TaskSubmitted(taskName) => println(taskName)
  }

  override protected def onError(throwable: Throwable): Unit = {

  }

  override protected def onStart(): Unit = {
    println("on start invoke")
  }
}
