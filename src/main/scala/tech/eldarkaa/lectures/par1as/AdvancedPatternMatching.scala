package tech.eldarkaa.lectures.par1as

import tech.eldarkaa.lectures.par1as.AdvancedPatternMatching.Person.unapply
import tech.eldarkaa.lectures.par1as.AdvancedPatternMatching.{even, singleDigit}

object AdvancedPatternMatching extends App {
  val numbers = List(1,2,3)
  val description:Unit = numbers match {
    case head :: Nil => println(s"the only element is $head.")
    case ::(head, next) =>
    case Nil =>
  }

  /*
    - constants
    - wildcards
    - case classes
    - tuples
    - some special magic like above
   */

  sealed trait Color
  object Color {
    case object Yellow extends Color
    case object Red extends Color

    def parseColor(color: Color): String = color match {
      case Yellow => "Yellow"
      case Red => "Red"
    }
  }

  // PM on standard class (with unnaply)
  class Person(val name: String, val age:Int)
  object Person {
    def unapply(person: Person): Option[(String, Int)] = Some(person.name, person.age)
    def unapply(age: Int): Option[String] = Some(if (age<21) "minor" else "major")
  }

  val bob = new Person("Bob", 25)

  val greeting = bob match {
    case Person(n,a) => s"Hi, my name is $n and I'm $a years old"
  }
  println(greeting)

  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status"
  }
  println(legalStatus)
  // HOW IT WORKS
  // runtime looks for method unnaply in Person

  /*
    Exercise.
   */

  object even {
    def unapply(value: Int): Boolean = value % 2 == 0
  }
  object singleDigit {
    def unapply(value: Int): Boolean = value > -10 && value < 10
  }
  val n: Int = 46
  val mathProperty = n match {
    case singleDigit() => "single digit"
    case even() => "an even number"
    case _ => "no property"
  }
  println(mathProperty)

  //infix patterns
  case class Or[A,B](a: A, b:B) // Either
  val either = Or(2, "two")
  val humanDescription = either match {
    case number Or string => s"$number is writter as $string"
    case _ => "wtf"
  }
  println(humanDescription)

  // decomposing sequences
  val vararg = numbers match {
    case List(1, _*) => "staring with 1" // List(1,2) or List(1,Nil) or List (1, List(1,2,..))
    case _ =>
  }
  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }
  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {

    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: MyList[Int] = Cons(1,Cons(2,Cons(3,Empty)))
  val decomposed = myList match {
    case MyList(1, 2, _*) => "starting with 1 and 2"
    case _ => "else"
  }
  println(decomposed)

  // custom return types for unapply
  // isEmpty: boolean, get: something

  abstract class Wrapper[T]{
    def isEmpty: Boolean
    def get: T
  }
  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      override def isEmpty: Boolean = false
      override def get: String = person.name
    }
  }
  println(bob.name)
  println(bob match {
    case PersonWrapper(name) => s"This person's name is $name"
    case _ => "An alien"
  })
}
