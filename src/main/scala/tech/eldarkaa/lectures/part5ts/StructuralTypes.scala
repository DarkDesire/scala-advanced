package tech.eldarkaa.lectures.part5ts

object StructuralTypes extends App {

  // legacy
  type JavaClosable = java.io.Closeable
  // new one
  class HipsterClosable {
    def close(): Unit = println("yeah yeah I'm closing")
    def closeSilently(): Unit = println("not making a sound")
  }
  //both have close: Unit

  // def closeQuietly(closeable: JavaClosable OR HipsterClosable)

  type UnifiedCloseable = {
    def close(): Unit
  } // STRUCTURAL TYPE

  def closeQuietly(unifiedCloseable: UnifiedCloseable): Unit =
    unifiedCloseable.close()

  closeQuietly(new JavaClosable{
    def close(): Unit = ???
  })
  closeQuietly(new HipsterClosable)



  // TYPE REFINEMENTS
  type AdvancedCloseable = JavaClosable {
    def closeSilently() : Unit
  }

  class AdvancedJavaCloseable extends JavaClosable {
    def close(): Unit = println("Java closes")
    def closeSilently(): Unit = println("Java closes silently")
  }

  def closeShh(advancedCloseable: AdvancedCloseable): Unit =  advancedCloseable.closeSilently()
  closeShh(new AdvancedJavaCloseable)
  // not ok: closeShh(new HipsterClosable)



  // using structural types as standalone types
  def altClose(closeable: {def close(): Unit}): Unit = closeable.close()


  // type-checking => duck typing

  type SoundMaker = {
    def makeSound(): Unit
  }
  class Dog {
    def makeSound(): Unit = println("bark!")
  }
  class Car {
    def makeSound(): Unit = println("vrooom!")
  }
  val dog: SoundMaker = new Dog
  val car: SoundMaker = new Car
  // static duck typing

  // CAVEAT: based on reflection

  // Exercise 1.
  trait CBL[+T] {
    def head: T
    def tail: CBL[T]
  }

  class Human {
    def head: Brain = new Brain
  }
  class Brain {
    override def toString: String = "BRAINZ"
  }

  def f[T](somethingWithAHead: {def head: T}): Unit = println(somethingWithAHead.head)

  /* Question.
    1. f is compatible with CBL and with a Human
    Answer: Yes
   */


  // Exercise 2.
  object HeadEqualizer {
    type Headable[T] = { def head: T }
    def ===[T](a: Headable[T], b: Headable[T]): Boolean = a.head() == b.head()
  }
  /* Question.
    2. is compatible with CBL and with a Human
    Answer: Yes
   */
  val brainzList = List(new Brain, new Brain)
  val stringsList = List("Brainz", Nil)
  HeadEqualizer.===(brainzList, new Human)
  // problem!
  HeadEqualizer.===(new Human, stringsList)
  // not type safe
}
