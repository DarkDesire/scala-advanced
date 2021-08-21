package tech.eldarkaa.lectures.part4implicits

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MagnetPattern extends App {

  // method overloading

  class Serializer[T]
  class P2PRequest
  class P2PResponse
  trait Actor {
    def receive(statusCode: Int): Int
    def receive(request: P2PRequest): Int
    def receive(request: P2PResponse): Int
    def receive[T:Serializer](message:T): Int
    def receive[T:Serializer](message:T, statusCode: Int): Int
    def receive(future: Future[P2PRequest]): Int
    // ! def receive(future: Future[P2PResponse]): Int
    // lots of overload
  }
  /* PROBLEMS
    1 - type erasure
    2 - lifting doesn't work for all overloads
      val receiveFV = receive _
    3 - code duplication
    4 - type inference and default args
      actor.receive(?!)
   */

  trait MessageMagnet[Result] {
    def apply(): Result
  }
  def receive[R](magnet: MessageMagnet[R]) = magnet()

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
    def apply(): Int = {
      // logic for handling a P2PRequest
      println("Handling P2PRequest")
      42
    }
  }

  implicit class FromP2PResponse(response: P2PResponse) extends MessageMagnet[Int] {
    def apply(): Int = {
      // logic for handling a P2PResponse
      println("Handling P2PResponse")
      24
    }
  }

  // 1 - no more type erasure
  println("no more type erasure")
  receive(new P2PRequest)
  receive(new P2PResponse)


  implicit class FromResponseFuture(future: Future[P2PResponse]) extends MessageMagnet[Int] {
    def apply(): Int = 2
  }
  implicit class FromRequestFuture(future: Future[P2PRequest]) extends MessageMagnet[Int] {
    def apply(): Int = 3
  }

  println(receive(Future(new P2PResponse)))
  println(receive(Future(new P2PRequest)))

  // 2 - lifting works

  trait MathLib {
    def add1(x: Int): Int = x+1
    def add1(s: String): Int = s.toInt + 1
  }

  // "magnetize"
  trait AddMagnet {
    def apply(): Int
  }
  def add1(magnet: AddMagnet): Int = magnet()
  implicit class AddInt(x:Int) extends AddMagnet {
    def apply(): Int = x+1
  }
  implicit class AddString(s:String) extends AddMagnet {
    def apply(): Int = s.toInt+1
  }

  val addFV = add1 _
  println("addFV (lifting works)")
  println(addFV(1))
  println(addFV("3"))


  // val receiveFV = receive _


  /* Magnet Pattern Drawbacks
    1 - API verbose
    2 - harder to read
    3 - you can't name or place default arguments
    4 - you can't receive nothing
    5 - call by name doesn't work correctly
   */

  // 5
  class Handler {
    def handle(s: => String) = {
      println(s)
      println(s)
    }
  }
  trait HandleMagnet {
    def apply(): Unit
  }
  def handle(magnet: HandleMagnet):Unit = magnet()

  implicit class StringHandle(s: => String) extends HandleMagnet {
    def apply(): Unit = {
      println(s)
      println(s)
    }
  }

  def sideEffectMethod(): String = {
    println("Hello, Scala")
    "magnet"
  }

  println("--------------- call by name behaviour !!!!")
  println("1:")
  handle(sideEffectMethod())
  println("2:")
  handle{
    println("Hello, Scala")
    "magnet"
  }
  println("3:")
  handle{
    println("Hello, Scala")
    new StringHandle("magnet")
  }
}
