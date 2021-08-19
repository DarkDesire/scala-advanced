package tech.eldarkaa.lectures.part3concurrency

object JVMThreadsExercises extends App {
  /*
  Exercises.
  1) think of an example where notifyAll acts in a different way than notify?
  2) create a deadlock
  2) create a livelock
   */

  // 1- notifyAll case
  def testNotifyAll(): Unit = {
    val bell = new Object

    (1 to 10).foreach(i => new Thread(() =>
      bell.synchronized{
        println(s"[th-$i] waiting")
        bell.wait()
        println(s"[th-$i] hooray!")
      }
    ).start())

    new Thread(() => {
      Thread.sleep(2000)
      println("[announcer] Rock'n roll!")
      bell.synchronized{
        bell.notifyAll() // vs bell.notify()
      }
    }).start()
  }
  // testNotifyAll()

  // 2 - deadlock case
  def deadlock(): Unit = {

    case class Friend(name: String){
      def bow(other: Friend) = {
        this.synchronized{
          println(s"$this: I'm bowing to my friend $other")
          other.rise(this)
          println(s"$this: my friend $other has risen")
        }
      }

      def rise(other: Friend) = {
        this.synchronized{
          println(s"$this: I'm rising to my friend $other")
        }
      }
    }
    val sam = Friend("Sam")
    val pierre = Friend("Pierre")

    new Thread(() =>{sam.bow(pierre)}).start() // sam's lock      | then pierre's lock
    new Thread(() =>{pierre.bow(sam)}).start() // pierre's lock   | then sam's lock
  }

  // 3 - livelock case
  def livelock(): Unit = {

    case class Friend(name: String){
      var side = "right"
      def switchSide(): Unit = {
        if (side == "right") side = "left"
        else side = "right"
      }

      def pass(other: Friend): Unit = {
        while (this.side == other.side) {
          println(s"$this: Oh, but please, $other, feel free to pass...")
          switchSide()
          Thread.sleep(1000)
        }
      }
    }
    val sam = Friend("Sam")
    val pierre = Friend("Pierre")

    new Thread(() => sam.pass(pierre)).start()
    new Thread(() => pierre.pass(sam)).start()
  }
  livelock()
}

