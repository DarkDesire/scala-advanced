package tech.eldarkaa.lectures.part4implicits

object PimpMyLibrary extends App {

  // 2 .isPrime

  implicit class RichInt(val value:Int) extends AnyVal {
    def isEven: Boolean = value % 2 == 0
    def sqrt: Double = Math.sqrt(value)
    def times[A](f: () => Unit):Unit =
      if (value>0) (1 to value).foreach(_ => f())
    def *[A](list: List[A]): List[A] = List.fill(value)(list).flatten
  }
  /*implicit class RicherInt(val richInt: RichInt) extends AnyVal {
    def isOdd: Boolean = richInt.value % 2 != 0
  }*/
  new RichInt(42).sqrt
  println(42.sqrt)
  println(42.isEven) // new RichInt(42).isEven
  // println(42.isOdd) //compiler doesn't do multiple implicits searches

  // type enrichment = pimping

  1 to 10
  import scala.concurrent.duration._
  3.seconds


  /*
  Exercise.
  Enrich the String class
   -asInt
   -encrpyt (John -> Lnjp 2 cypher)

   Keep enriching the Int class
    -times(f)
        3.times(() => ....)
    - *
        3 * List(1,2) => List(1,2,1,2,1,2)
   */

  implicit class RichString(val value: String) extends AnyVal {
    def asInt: Int = Integer.parseInt(value)
    def encrypt(distance:Int): String = value.map(c => (c.toInt+distance).asInstanceOf[Char])
  }

  println("42".asInt)
  println("John".encrypt(2))

  5.times(() => println("hello"))
  println(3*List(1,2,3))

  println(5*List("a","b"))

  implicit def stringToInt(str: String): Int = Integer.parseInt(str)
  println("3" / 4d)

  // equivalent: implicit class RichAltInt(value:Int)
  class RichAltInt(value:Int)
  implicit def enrich(value: Int): RichAltInt = new RichAltInt(value)

  // danger zone
  implicit def intToBoolean(i: Int): Boolean = i == 1

  /*
    if (i) do something
    else do something else
   */

  val aConditionValue = if (intToBoolean(1)) "OK" else "Something wrong"
  println(aConditionValue)
}
