package top.vabook.thread

import java.util
import java.util.concurrent.{Callable, Executors}

object ThreadDemo {
  def main(args: Array[String]): Unit = {
    //设置线程的数量
    val pool = Executors.newFixedThreadPool(5)
    pool.execute((new Runnable {
      override def run(): Unit = {
        println(Thread.currentThread().getName)
        Thread.sleep(1000)
      }
    }))

    //callable 个runnable都是启动一个线程,但是callable可以有返回值
    val f: util.concurrent.Future[Int] = pool.submit(new Callable[Int] {
      override def call(): Int = {
        Thread.sleep(1000)
        100
      }
    })
    var status = f.isDone
    println(s"task status $status")

    if (status){
      println(f.get())
    }

    Thread.sleep(1500)
    status = f.isDone
    println(s"task status $status")

    //get()方法拿到返回值
    if (status){
      println(f.get())
    }
  }
}
