package tech.eldarkaa.lectures.part3concurrency

object WaitAndNotify extends App {

  /*
  the producer-consumer problem
    producer -> [ ? ] -> consumer
   */

  // wait() and notify()
  // wait()-ing on a object's monitor suspends you (the thread) indefinitely

  def waitAndNotify() = {
    // thread 1
    val someObject = "hello"
    someObject.synchronized{   // lock the object's monitor
      // code part1
      someObject.wait() // release the lock
      // code part2     // when allowed proceed, lock the monitor and continue
    }

    // thread2
    someObject.synchronized{ // lock the object's monitor
      // code ...
      someObject.notify() // signal ONE sleeping thread they may continue
      // use notifyAll() to awaken ALL threads
      // more code ...
    } // but only after I'm done and unlock the monitor

  }

  def smartProdCons():Unit = {
    class SimpleContainer{
      private var value: Int = 0
      def isEmpty: Boolean = value == 0
      def get:Int = {
        val result = value
        value = 0
        result
      }
      def set(newValue: Int):Unit = value = newValue
    }
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] is waiting")
      container.synchronized{
        container.wait()
      }
      // container must have some value
      println("[consumer] I have consumed " + container.get)

    })


    val producer = new Thread(() => {
      println("[producer] Hard at work.. ")
      Thread.sleep(2000)
      val value = 42

      container.synchronized{
        println("[producer] I have produced, after long work, the value: " + value)
        container.set(value)
        container.notify()
      }
    })

    consumer.start()
    producer.start()
  }
  smartProdCons()
}
