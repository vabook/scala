package top.vabook.thread

import java.util.concurrent.{Callable, Executors}

object ThreadPool02 {
  def main(args: Array[String]): Unit = {
    val threadPool = Executors.newFixedThreadPool(5)
    val synTask = new Callable[Int] {
      override def call(): Int = {
        var i = 0
        println("start cpmpute")
        Thread.sleep(1000)
        i = 1000
        i
      }
    }
    val future = threadPool.submit(synTask)
    val result = future.get()
    println(result)
  }
}
