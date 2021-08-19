package tech.eldarkaa.lectures.part3concurrency

object ThreadCommunication extends App {

  /*
  the producer-consumer problem

  producer (set value) ->
                      [ ? ]
                       -> (extract value) consumer

   */

  def naiveProdCons():Unit = {
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
      while (container.isEmpty) {
        println("[consumer] actively waiting")
      }
      println("[consumer] I have consumed " + container.get)

    })


    val producer = new Thread(() => {
      println("[producer] computing.. ")
      Thread.sleep(500)
      val value = 42
      println("[producer] I have produces, after long work, the value: " + value)
      container.set(value)
    })

    consumer.start()
    producer.start()
  }
  //naiveProdCons()
  // dumb waiting
  // better use wait and notify

  val someObjectHello = "Hello"
  someObjectHello.synchronized{ // lock the object's monitor
    //business code <- any other thread trying to run this will be blocked
  } // release the lock

  // only AnyRes can have synchronized blocks.

  // General principles:
  // make no assumptions about who get the lock first
  // keep locking to a minimum
  // maintain thread safety at ALL TIME in parallel applications


}
