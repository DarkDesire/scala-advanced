package tech.eldarkaa.lectures.part4implicits

object TypeClasses extends App {
  // Tips:
  /*
    - keep type enrichment to implicit classes and type classes
    - avoid implicit defs as much as possible
    - package implicits clearly ,bring into the scope ONLY WHAT YOU NEED
    - IF you need conversion, make them specific
   */

  // option 1 - trait
  trait HTMLWritable {
    def toHTML: String
  }
  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHTML: String = s"<div>$name ($age yo) <a href=$email/> </div>"
  }
  val john = User("John", 32, "john@rockthejvm.com")
  john.toHTML

  /*
   1- for the types WE write
   2 - one implementation out of quite a number
   */

  // option 2 - pattern matching
  object HTMLSerializerPM {
    def serializeToHtml(value: Any) = value match {
      case User(n,a,e) =>
      case _ =>
    }
  }
  /*
    1 - lost type safety
    2 - need to modify the code every
    3-  still one implementation
   */


  // better design
  trait HTMLSerializer[T]{
    def serialize(value: T): String
  }
  implicit object UserSerializer extends HTMLSerializer[User]{
    override def serialize(user: User): String =
      s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"
  }
  println(UserSerializer.serialize(john))

  // 1 - we can define serializer for other types
  import java.util.Date
  object DateSerializer extends HTMLSerializer[Date]{
    override def serialize(date: Date): String = s"<div>${date.toString}</div>"
  }
  println(DateSerializer.serialize(new Date(2021,0,1)))

  // 2 - we can define MULTIPLE serializers
  object PartialUserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String =
      s"<div>${user.name}</div>"
  }

  // TYPE CLASS - trait[T]
  // object = TypeClass[T] - type class instances
  // we instantiate them only ONCE



  /*
   Equality
   */

  trait Equal[T] {
    def ===(a:T, b:T): Boolean
  }
  object NameEquality extends Equal[User]{
    def ===(a: User, b: User): Boolean = a.name == b.name
  }
  object EmailEquality extends Equal[User]{
    def ===(a: User, b: User): Boolean = a.email == b.email
  }
  val anna = User("Anna", 33, "john@rockthejvm.com")
  println(s"name equality: ${NameEquality.===(john, anna)}")
  println(s"email equality: ${EmailEquality.===(john, anna)}")
  // wtf John, emails are the same


  // part 2
  object HTMLSerializer {
    def serialize[T](value:T)(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
  }
  implicit object IntSerializer extends HTMLSerializer[Int]{
    def serialize(value: Int): String = s"<div style: color=blue>$value</div>"
  }
  implicit object StringSerializer extends HTMLSerializer[String]{
    def serialize(value: String): String = s"<div style: displat-text=inline>$value</div>"
  }
  println(HTMLSerializer.serialize(42))
  println(HTMLSerializer.serialize(john))

  // apply - access to the entire type class interface
  println(HTMLSerializer[User].serialize(john))


  // TYPE CLASS
  trait MyTypeClassTemplate[T] {
    def action(value: T):String
  }
  object MyTypeClassTemplate {
    def apply[T](implicit instance: MyTypeClassTemplate[T]) = instance
  }



  // Exercise
  /*
    Implement the TC pattern for the Equality
   */
  /* trait Equal[T] {
    def ===(a:T, b:T): Boolean
  } */
  object Equal {
    def apply[T](a:T, b:T)(implicit eq: Equal[T]) = eq.===(a,b)
  }
  implicit object IntEqual extends Equal[Int] {
    def ===(a: Int, b: Int): Boolean = a == b
  }
  implicit object UserEqual extends Equal[User] {
    def ===(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
  }

  println(Equal(1,2))
  println(Equal(2,2))
  println(Equal(john,anna))
  // AD-HOC polymorphism

}
