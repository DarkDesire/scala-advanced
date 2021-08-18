package tech.eldarkaa.lectures.part3concurrency

import java.util.concurrent.Executors

object Intro extends App {

  // JVM Thread
  /*
  interface Runnable {
    public void run()
  }
  */

  // JVM Threads
  val runnable = new Runnable {
    def run(): Unit = println(s"$threadName | runnable")
  }
  val aThread = new Thread(runnable)
  // create a JVM thread => OS thread
  aThread.start() // gives the signal to the JVM to start a JVM thread / OS thread
  aThread // thread instance

  runnable.run() // doesn't do anything in parallel // run in main
  aThread.join() // blocks until Thread a finishes running

  def threadName: String = Thread.currentThread().getName

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println(s"$threadName | hello")))
  val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println(s"$threadName | goodbye")))
  threadHello.start()
  threadGoodbye.start()
  /* different runs produces different results
  goodbye
  hello
  hello..
   */

  // !!!
  // starting thread manually so boring,
  // so we have Executors and Pools in Java

  // executors
  val pool = Executors.newFixedThreadPool(10)
  val smthInThreadPool = new Runnable {
    def run(): Unit = println(s"$threadName | something in the thread pool")
  }
  pool.execute(smthInThreadPool)

  // will run in different thread
  pool.execute(() => {
    Thread.sleep(1000)
    println(s"$threadName | done after 1 second")
  })
  pool.execute(() => {
    Thread.sleep(1000)
    println(s"$threadName | almost done")
    Thread.sleep(1000)
    println(s"$threadName | done after 2 seconds")
  })

  pool.shutdown()
  // pool.shutdownNow() // force, sleep interrupted

  // throws an exception in the calling thread
  //pool.execute(() => println("should not appear"))
  println(pool.isShutdown)
}
