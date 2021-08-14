package tech.eldarkaa.lectures.par2afp

object PartialFunctions extends App {

  class FunctionNotApplicableException extends RuntimeException

  val aFunction = (x:Int) => x+1
  val aFussyFunction = (x:Int) => // Function1[Int,Int] === Int => Int
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  val aNicerFussyFunction = (x:Int) => x match {
    case _ => throw new FunctionNotApplicableException
  }

  // PARTIAL FUNCTION
  // {1,2,5} => Int
  val aPartialFunction: PartialFunction[Int,Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } // partial function value

  println(aPartialFunction(2))

  // PF utilities
  // isDefinedAt
  println(aPartialFunction.isDefinedAt(67))
  // lift ( Int => Option[Int] )
  val lifted = aPartialFunction.lift
  println(lifted(2))
  println(lifted(67))

  // chain
  val anotherPartialFunction: PartialFunction[Int,Int] = {
    case 45 => 67
  }
  val pfChain = aPartialFunction.orElse(anotherPartialFunction)
  println(pfChain(2))
  println(pfChain(45))

  // PF extend normal functions
  val aTotalFunction: Int => Int = {
    case 1 => 99
  }

  // HOFs accept partial functions as well
  val aMappedList = List(1,2,3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 100
  }
  println(aMappedList)

  /*
    Note: PF can only have ONE parameter type
    1. construct a PF instance yourself (anonymous class)
    2. dumb chatbot as a PF
   */

  class AnotherPF[-A,+B] extends PartialFunction[A,B] {
    def isDefinedAt(x: A): Boolean = ???
    def apply(v1: A): B = ???
  }

  val aManualFussyFunction = new PartialFunction[Int, Int] {
    def isDefinedAt(x: Int): Boolean = x == 1 | x == 2 | x == 5
    def apply(x: Int): Int = x match {
      case 1 => 42
      case 2 => 65
      case 5 => 99
    }
  }

  val chatBotPF: PartialFunction[String, String] = {
    case "hello" => "hi there!"
    case "my_ip" => "127.0.0.1"
  }

   io.Source.stdin.getLines().map(chatBotPF).foreach(println)
}
