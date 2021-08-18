package tech.eldarkaa.lectures.part3concurrency

object JVMThreadExercises extends App{

  /*
    Exercise.
    1) Construct 50 "inception" threads
     Thread1 -> thread2 -> thread3
     println("hello from thread #3")
     in REVERSE ORDER

     Hint: start, join
  */
  def inceptionThreads(maxDeep: Int = 50, currentDeep: Int = 1): Thread =
    new Thread(() => {
      if (currentDeep < maxDeep) {
        val thread = inceptionThreads(maxDeep, currentDeep + 1)
        thread.start()
        thread.join()
      }
      println(s"Hello from thread: $currentDeep")
  })
  inceptionThreads().start()

  // 2)
  var x = 0
  val threads = (1 to 100).map(_ => new Thread(() => x+=1))
  threads.foreach(_.start())
  // println(x)
  // Questions?
  // what is the biggest value possible for x? // 100
  // what is the SMALLEST values possible for x? // 1

  /*
    3) sleep fallacy
   */
  var message = ""
  val awesomeThread = new Thread(() =>{
    Thread.sleep(1000)
    message = "Scala is awesome"
  })

  message = "Scala sucks"
  awesomeThread.start()
  Thread.sleep(1001)
  awesomeThread.join()
  println(message)
  // println(message)
  // Question.
  // What's the value? "Scala is awesome" almost always
  // Is it guaranteed? No
  // Why? or why not?
  /*
    (main thread)
    message = "Scala sucks"
    awesomeThread.start()
      - sleep() - relieves execution
    (awesome thread)
      - sleep() - relieves execution
    (OS gives the CPU to some important thread - takes CPU for more than 2 sec)
    (OS gives the CPU back to the MAIN thread)
      - println("Scala sucks")
    (OS gives the CPU back to the awesomeThread)
      - message = "Scala is awesome"
   */

  // how do we fix this?
  // synchronized does NOT work
  // ! WITH THREAD JOIN (wait for the awesome thread to join)
}
