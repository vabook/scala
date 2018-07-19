package top.vabook.thread

object Thread01 {
  def main(args: Array[String]): Unit = {
    val thread = new Thread(
      new Runnable {
        override def run(): Unit = {
       for ( i <- 1 to 100) println(i)
        }
      })
    thread.setDaemon(true)
    thread.start()
    println("123")
  }
}
