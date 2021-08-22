package tech.eldarkaa.lectures.part5ts

object RockingInheritance extends App {
  // IO library
  // convenience
  trait Writer[T] {
    def write(value:T): Unit
  }
  trait Closeable {
    def close(status: Int): Unit
  }
  trait GenericStream[T] {
    // some methods
    def foreach(f: T => Unit): Unit
  }

  // 1
  def processStream[T](stream: GenericStream[T] with Writer[T] with Closeable): Unit = {
    stream.foreach(println)
    stream.close(0)
  }
   // 2 diamond problem
  trait Animal { def name: String }
  trait Lion extends Animal { override def name: String = "lion" }
  trait Tiger extends Animal { override def name: String = "tiger" }
  trait MutantTrait extends Lion with Tiger
  class Mutant extends Lion with Tiger // LAST OVERRIDE GETS PICKED
  println(new Mutant().name)

  // 3 the super problem + type linearization

  trait Cold {
    def print = println("cold")
  }
  trait Green extends Cold{
    override def print: Unit = {
      println("green")
      super.print
    }
  }
  trait Blue extends Cold{
    override def print: Unit = {
      println("blue")
      super.print
    }
  }

  class Red {
    def print = println("red")
  }

  class White extends Red with Green with Blue {
    override def print: Unit = {
      println("white")
      super.print
    }
  }
  //white blue green cold
  val wh = new White()
  wh.print

  // Who is my super ?
  // [Green, Blue] => Cold (super)
  // [Red, Green, Blue] => White (bottom)


  /* Cold = AnyRef with <Cold>

    Green
      = Cold with <Green>
      = AnyRef with <Cold> with <Green>
    Blue
      = Cold with <Blue>
      = AnyRef with <Cold> with <Blue>
    Red
      = AnyRef with <Red>

    White = Red with Green with Blue with <White>
      = AnyRef with <Red>
        with (AnyRef with <Cold> with <Green>)
        with (AnyRef with <Cold> with <Blue>)
        with <White>
    (after compile do some magic)
    White = AnyRef with <Red> with <Cold> with <Green> with <Blue> with <White>
    (type linearization)
    super of White => Blue
    super of Blue => Green
    super of Green => Cold

    wh.print
      white
      blue
      green
      cold
  */
}
