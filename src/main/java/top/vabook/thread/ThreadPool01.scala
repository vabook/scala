package top.vabook.thread

import java.util.concurrent.{ExecutorService, Executors}

object ThreadPool01 {
  def main(args: Array[String]): Unit = {
    val threadPool01 : ExecutorService = Executors.newFixedThreadPool(5)
    val task = new Runnable {
      override def run(): Unit = {
        println(Thread.currentThread().getName)
        println("submitting task")
        Thread.sleep(1000)
        println("task finished")
      }
    }

    println(Thread.currentThread().getName)

    println("main ---pre")
    threadPool01.execute(task)
    println("main -- post")

    threadPool01.shutdownNow()
  }
}
