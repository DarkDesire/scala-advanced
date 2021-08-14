package tech.eldarkaa.lectures.par1as

import scala.util.Try

object DarkSugars extends App {

  // syntax sugar #1: methods with single param
  def singleArgMethod(arg:Int): String = s"$arg little ducks"

  val description = singleArgMethod {
    // write some code
    println("side effect")
    42
  }
  val aTryInstance = Try { // java's try {... }
    throw new RuntimeException
  }

  List(1,2,3).map{x=> x+1}

  // syntax sugar #2: single abstract method
  trait Action {
    def act(x:Int): Int
  }
  val anInstance: Action = new Action {
    def act(x: Int): Int = x + 1
  }
  val aFunkyInstance: Action = (x:Int) => x+1

  // example: Runnables
  val aThread = new Thread(new Runnable {
    def run(): Unit = println("Hello, Scala")
  })
  val aSweeterThread = new Thread(() => println("s sweet, Scala"))

  abstract class AnAbstractType {
    def implemented: Int = 23
    def f(a:Int): Unit
  }
  val anAbstractInstace:AnAbstractType = (a:Int) => println("sweet")
  println(anAbstractInstace.f(42))

  // syntax sugar #3: the :: and #:: methods are special
  val prependedList = 2 :: List(3,4)
  // 2.::(List[3,4])
  // List(3,4).::(2)
  // ?!
  // scala specification: last character associativity of method :(:), #:(:)
  val test = 1 :: 2 :: 3 :: List(4,5)
  val test2 = List(4,5).::(3).::(2).::(1) // equivalent

  class MyStream[T]{
    def -->:(value:T): MyStream[T] = this
    def -!>:(value:T): MyStream[T] = this
  }
  val myStream = 1 -!>: 2 -->: 3 -!>: new MyStream[Int]

  // syntax sugar #4: multi-word method naming
  class TeenGirl(name: String){
    def `and then said`(gossip: String): Unit = println(s"$name said message $gossip")
  }
  val alisha = new TeenGirl("Alisha")
  alisha `and then said` "Scala is so sweet"

  // syntax sugar #5: infix types
  class Composite[A,B]
  val composite: Composite[Int, String] = ???
  val composite2: Int Composite String = ???

  class -->[A,B]
  val towards: Int --> String = ???

  // syntax sugar #6: update() is very special, much like apply

  val array = Array(1,2,3)
  array(2) = 7 // rewritten to array.update(2,7)
  // used in mutable collections
  // remember apply() AND update()


  // syntax sugar #7: setters for mutable containers
  class Mutable {
    private var internalMember: Int = 0 // private for OO encapsulation
    def member = internalMember // "getter"
    def member_=(value:Int): Unit = // "setter"
      internalMember = value
  }
  val aMutableContainer = new Mutable
  aMutableContainer.member = 42 // rewritten to aMutableContainer.member_= 42

}
