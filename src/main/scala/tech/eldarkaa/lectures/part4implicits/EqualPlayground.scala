package tech.eldarkaa.lectures.part4implicits

import tech.eldarkaa.lectures.part4implicits.TypeClasses.{User}

object EqualPlayground extends App {

  /*
   Equality
   */

  trait Equal[T] {
    def ===(a:T, b:T): Boolean
  }
  implicit object NameEquality extends Equal[User]{
    def ===(a: User, b: User): Boolean = a.name == b.name
  }
  object EmailEquality extends Equal[User]{
    def ===(a: User, b: User): Boolean = a.email == b.email
  }
  val john = User("John", 32, "john@rockthejvm.com")
  val anna = User("Anna", 33, "john@rockthejvm.com")
  println(s"name equality: ${NameEquality.===(john, anna)}")
  println(s"email equality: ${EmailEquality.===(john, anna)}")
  // wtf John, emails are the same

  // Exercise
  /*
    Implement the TC pattern for the Equality
   */
  object Equal {
    def apply[T](a:T, b:T)(implicit equal: Equal[T]): Boolean = equal.===(a,b)
  }

  // AD-HOC polymorphism


  /* Exercise
  * improve the EQUAL TC with implicit conversion class
  * ===(another value: T)
  * !==(another value: T) =
  * */
  implicit class EnrichEquality[T](value:T){
    def ===(another: T)(implicit equal: Equal[T]):Boolean = equal.===(value, another)
    def !==(another: T)(implicit equal: Equal[T]):Boolean = !equal.===(value, another)
  }

  println(john === anna)
  /*
    john.===(anna) (no such method)
    try wrap john => new EnrichEquality[User](john)
    new EnrichEquality[User](john).===(anna)
    new EnrichEquality[User](john).===(anna)(NameEquality)
   */
  /*
    TYPE SAFE
   */
  println(john == 43)
  // not compile, must be of the same type
  // println(john === 43)
  // TYPE SAFE
}
