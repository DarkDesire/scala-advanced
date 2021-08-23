package tech.eldarkaa.lectures.part5ts

object TypeMembers extends App {

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection {
    // abstract type members
    type AnimalType
    type BoundedAnimal <: Animal
    type SuperBoundedAnimal >: Dog <: Animal
    type AnimalC = Cat
  }
  val ac = new AnimalCollection
  val dog: ac.AnimalType = ??? // doesn't work
  // val cat: ac.BoundedAnimal = new Cat // doesn't work

  val pup: ac.SuperBoundedAnimal = new Dog
  val cat: ac.AnimalC = new Cat

  // type aliases
  type CatAlias = Cat
  val anotherCat: CatAlias = new Cat



  // alternative to generics
  trait MyList {
    type T
    def add(element: T): MyList
  }
  class NonEmptyList(value: Int) extends MyList{
    type T = Int
    def add(element: Int): MyList = ???
  }

  // .type
  // type alias
  type CatsType = cat.type
  val newCat: CatsType = cat // cannot instantiate new
  // ! new CatsType // err! class type required but TypeMembers.cat.type found new CatsType


  /* Exercise
    enforce a type to be applicable to SOME TYPES only
   */

  // LOCKED
  trait MList {
    type A >: AnyVal
    def head: A
    def tail: MList
  }

  trait ApplicableToNumbers {
    type A <: Number
  }

  // NOT OK
  /*class CustomList(hd: String, tl: CustomList) extends MList with ApplicableToNumbers {
    type A = String
    def head: A = hd
    def tail: CustomList = tl
  }*/
  // OK
  class IntList(hd: Int, tl: IntList) extends MList {
    type A = Int
    def head: A = hd
    def tail: MList = tl
  }




}
