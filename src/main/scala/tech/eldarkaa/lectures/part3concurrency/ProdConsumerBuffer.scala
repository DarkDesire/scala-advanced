package tech.eldarkaa.lectures.part3concurrency

import scala.collection.mutable
import scala.util.Random

object ProdConsumerBuffer extends App {
  /*
  the producer-consumer problem
    producer -> [ ? ? ? ] -> consumer
   */
  def prodConsLargeBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity: Int = 3

    val consumer = new Thread(() => {
      val random = new Random()

      while(true){
        buffer.synchronized{
          if (buffer.isEmpty) {
            println("[consumer] buffer empty, waiting")
            buffer.wait()
          }
          // there must be at least ONE value in the buffer
          // (after notified)
          val x = buffer.dequeue()
          println(s"[consumer] I consumed: $x")
          // hey producer, there's empty space available, are u lazy?
          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    })
    val producer = new Thread(() => {
      val random = new Random()
      var i = 0
      while(true){
        buffer.synchronized{
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting ...")
            buffer.wait() // wait until consumer wake me up
          }
          // there must be at least ONE EMPTY SPACE in the buffer
          println(s"[producer] I have produced: $i")
          buffer.enqueue(i)
          // hey consumer, there's a new food for u
          buffer.notify()
          i+=1
        }
        Thread.sleep(random.nextInt(500))
      }
    })
    consumer.start()
    producer.start()
  }
  prodConsLargeBuffer()
}
