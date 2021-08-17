package tech.eldarkaa.lectures.par2afp

import scala.util.Random

object CurriesPAF extends App {

  // curried functions
  val supperAdder: Int => Int => Int =
    x=>y=>x+y
  val add3 = supperAdder(3) // Int => Int = y=>y+3
  println(add3(5)) // Int => Int = x=> x+add3
  println(supperAdder(3)(5))

  // METHOD!
  def curriedAdder(x: Int)(y: Int): Int = x+y   //curried method
  val add4: Int=> Int = curriedAdder(4)
  // lifting = ETA-EXPANSION
  println(add4(3))


  // functions != methods (JVM limitations)
  def inc(x: Int) = x+1
  List(1,2,3).map(inc) // ETA-expansion

  // Partial Function applications
  val add5 = curriedAdder(5) _
  println(add5(3))


  // Exercise
  val simpleAddFunction = (x: Int, y: Int) => x+y
  def curredAddMethod(x: Int)(y:Int) = x+y
  def simpleAddMethod(x: Int, y:Int) = x+y

  // ex, add7: Int => Int = y => 7+y
  val simple7 = (x:Int) => simpleAddFunction(7, x)
  val simple7v2 = simpleAddFunction.curried(7)
  val simple7v3 = simpleAddFunction(7, _:Int)

  val curried7func = curredAddMethod (7) _
  val curried7func2 = curredAddMethod (7) (_)

  val simpleMethod7 = (x:Int) => simpleAddMethod(7, x)
  val simpleMethod7v2 = simpleAddMethod(7, _:Int)
  // alternative syntax for turning methods into function values

  // underscores are powerful
  def concatenator(a: String, b: String, c: String) = a+b+c
  val insertName = concatenator("Hello, I'm ", _:String, ", how are you?")
  // x:String => concatenator(hello,x,how are you)
  println(insertName("Eldar"))

  val fillinTheBlanks = (concatenator("Hello, ", _:String, _:String))
  // (x, y) => concatenator(hello,x,y)
  println(fillinTheBlanks("Eldar", ". Scala is awesome!"))

  // Exercise.
  /*
    1. Process a list of numbers and return their string representation with
    different formats
    Use: %4.2f, %8.6f and %14.12f with a curried format
   */
  val numbers = List.fill(5)(Random.nextFloat()*100)
  def curriedFormatter(s: String)(number:Float) = s.format(number)
  val format_4_2f = curriedFormatter("%4.2f") _
  val format_8_6f = curriedFormatter("%8.6f") _
  val format_14_12f = curriedFormatter("%14.22f") _

  println(numbers.flatMap(f =>
    List(format_4_2f(f), format_8_6f(f), format_14_12f(f))
  ).mkString("\n"))

  /*
    2. difference between
        - functions vs methods
        - parameters: by-name vs 0-lambda
   */
  def byName(n: => Int) = n+1
  def byFunc(f: () => Int) = f()+1
  def method: Int = 42
  def parenthesisMethod(): Int = 42
  /*
    calling byName and byFunctions
    - int
    - method
    - parenMethod
    - lambda (() => ..)
    - PAF ( func _ )
   */

  // byName (uses value, not fn itself)
  byName(1) // ok
  byName(method) // ok
  byName(parenthesisMethod()) //
  byName(parenthesisMethod) // ok, but beware => byName(parenthesisMethod())

  // byName(() => 42) // not ok
  byName((() => 42)()) // ok, because we call it, then pass a value
  // byName(parenthesisMethod _) // not ok

  // -------------------------------------------
  // byFunction
  // byFunc(1) // not ok
  // byFunc(method) // not ok !!!!!!!!!!!!!!!!
  // compiler does do ETA-expansion !

  byFunc(parenthesisMethod) // does ETA-expansion
  byFunc(()=> 1)
  byFunc(parenthesisMethod _) // PAF, also works
}
