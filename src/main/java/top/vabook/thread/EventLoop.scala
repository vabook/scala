package top.vabook.thread

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue}

import scala.util.control.NonFatal

/**
  * @author vabook@163.com
  * @version 2018/7/12 15:52
  *
  */
abstract class EventLoop[E](name: String) {
  //java消息队列
  private val eventQueue: BlockingQueue[E] = new LinkedBlockingQueue[E]()

  private val stopped = new AtomicBoolean(false)

  private val eventThread = new Thread(name) {
    override def run(): Unit = {
      try {
        while (!stopped.get()) {
          val event = eventQueue.take()
          try{
            // 利用了偏函数
            onReceive(event)
          }catch{
            case NonFatal(e) => {
              try{
                onError(e)
              }catch{
                case NonFatal(e) => println("unexpected error " + name ,e)
              }
            }
          }
        }
      }catch{
        case ie : InterruptedException =>
        case NonFatal(e) => println("unexceoted error in " + name ,e)
      }
    }
  }
  def start() : Unit = {
    if (stopped.get()){
      throw new IllegalStateException(name + "has already been stopped")
    }
    onStart()
    eventThread.start()
  }
  def stop(): Unit ={
    if (stopped.compareAndSet(false,true)){
      eventThread.interrupt()
      var onStopCalled = false
      try{
        eventThread.join()

        onStopCalled = true
        onStop()
      }catch{
        case ie : InterruptedException =>
          Thread.currentThread().interrupt()
          if (!onStopCalled){
            onStop()
          }
      }
    }else{

    }
  }
  def  post(event : E): Unit ={
    eventQueue.put(event)
  }
  def isActive : Boolean = eventThread.isAlive

  protected def onStart(): Unit ={

  }
  protected def onStop(): Unit ={

  }
  protected def onReceive : PartialFunction[E , Unit]
  protected def onError(throwable: Throwable): Unit ={

  }
}
