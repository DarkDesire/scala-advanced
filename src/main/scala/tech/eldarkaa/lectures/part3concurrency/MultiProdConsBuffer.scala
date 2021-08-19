package tech.eldarkaa.lectures.part3concurrency

import scala.collection.mutable
import scala.util.Random

object MultiProdConsBuffer extends App {

  /*
  the producer-consumer problem
    producer(1-N) -> [ ? ? ? ] -> consumer(1-N)
   */

  val nConsumers = 3
  val nProducers = 3
  val bufferCapacity: Int = 20
  def multiProdConsLargeBuffer(nConsumers: Int, nProducers: Int, bufferCapacity:Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]

    class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread{
      override def run(): Unit = {
        val random = new Random()

        while (true) {
          buffer.synchronized {
            while (buffer.isEmpty) {
              println(s"[consumer-$id] buffer empty, waiting")
              buffer.wait()
            }
            // there must be at least ONE value in the buffer
            // (after notified)
            val x = buffer.dequeue()
            println(s"[consumer-$id] I consumed: $x")

            // notifies on of waiting on buffer
            buffer.notify()
          }
          Thread.sleep(random.nextInt(500))
        }
      }
    }
    class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread{
      override def run(): Unit = {
        val random = new Random()
        var i = 0
        while(true){
          buffer.synchronized{
            while(buffer.size == bufferCapacity) {
              println(s"[producer-$id] buffer is full, waiting ...")
              buffer.wait() // wait until consumer wake me up
            }
            // there must be at least ONE EMPTY SPACE in the buffer
            println(s"[producer-$id] I have produced: $i")
            buffer.enqueue(i)

            // notifies smbd to wake up on buffer
            buffer.notify()
            i+=1
          }
          Thread.sleep(random.nextInt(500))
        }
      }
    }

    (1 to nConsumers)
      .foreach(i => new Consumer(i, buffer).start())
    (1 to nProducers)
      .foreach(i => new Producer(i, buffer, bufferCapacity).start())
  }
  multiProdConsLargeBuffer(nConsumers, nProducers, bufferCapacity)
}
