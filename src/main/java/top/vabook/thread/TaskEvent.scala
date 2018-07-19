package top.vabook.thread

/**
  * @author vabook@163.com
  * @version 2018/7/12 15:45
  *
  */
trait TaskEvent {

}
case class TaskSubmitted(name : String) extends TaskEvent
case class TaskSucceeded(name : String) extends TaskEvent
case class TaskFailed(name : String) extends TaskEvent
