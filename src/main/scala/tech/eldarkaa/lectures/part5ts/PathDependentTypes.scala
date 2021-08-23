package tech.eldarkaa.lectures.part5ts

object PathDependentTypes extends App{

  class Outer {
    class Inner
    object InnerObject
    type InnerType

    def print(i: Inner) = println(i)
    def printGeneral(i: Outer#Inner) = println(i)
  }

  def aMethod: Int = {
    class HelperClass
    type HelperType = String
    2
  }

  // per-instance
  val o = new Outer
  val inner = new o.Inner // o.Inner is a TYPE

  val oo = new Outer
  // ! val iinner: oo.Inner = new o.Inner
  o.print(inner)
  // ! oo.print(inner) different types


  // path-dependent type
  // Outer#Inner
  o.printGeneral(inner)
  oo.printGeneral(inner)

  /*
    Exercise.
    DB keyed by INT or String, but maybe others
   */

  trait Item[K] extends ItemLike {type Key = K}
  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  trait ItemLike {type Key}

  def get [ItemType <: ItemLike](key: ItemType#Key) = ???
  get[IntItem](key = 42) // ok
  get[StringItem]("home") // ok
  // get[IntItem]("scala") // not ok
}
