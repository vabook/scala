package top.vabook.thread

/**
  * @author vabook@163.com
  * @version 2018/7/12 15:49
  * 消费入口
  */
object BootStrap {
  def main(args: Array[String]): Unit = {
    val eventLoop = new TaskProcessEventLoop("task-event-loop")
    eventLoop.start()
    for (i <- 1 to 10){
      eventLoop.post(TaskSubmitted(s"task-$i"))
    }
    Thread.sleep(1000)
  }
}
