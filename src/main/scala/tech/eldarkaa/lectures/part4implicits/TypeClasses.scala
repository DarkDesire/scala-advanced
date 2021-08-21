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
  case class User(name: String, age: Int, email: String)
  val john = User("John", 32, "john@rockthejvm.com")
  //john.toHTML

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



  // part 2
  object HTMLSerializer {
    def serialize[T](value:T)(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
    def apply[T](implicit serializer: HTMLSerializer[T]): HTMLSerializer[T] = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int]{
    def serialize(value: Int): String = s"<div style: color=blue>$value</div>"
  }
  implicit object StringSerializer extends HTMLSerializer[String]{
    def serialize(value: String): String = s"<div style: displat-text=inline>$value</div>"
  }
  implicit object UserSerializer extends HTMLSerializer[User]{
    def serialize(user: User): String =
      s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"
  }
  println(HTMLSerializer.serialize(42))
  println(HTMLSerializer.serialize(john))

  // apply - access to the entire type class interface
  println(HTMLSerializer[User].serialize(john))


  // part 3
  implicit class HTMLEnrichment[T](value: T) {
    def toHTML(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
  }
  println("HTMLEnrichment")
  println(john.toHTML) // new HTMLEnrichment(john).toHTML(UserSerializer)
  println(42.toHTML)
  // COOL
  /*
   -extend to new types!
   -choose implementation (pass explicitly)
   -super expressive!
   */
  println(john.toHTML(PartialUserSerializer))

  /*
   - type class itself (HTMLSerializer[T])
   - type class instances, some of which are implicit: (UserSerializer, IntSerializer)
   - conversion with implicit classes, which allow use implicit instances: (HTMLEnrichment)
   */


  // context bounds
  def htmlBoilerplate[T](content: T)(implicit serializer: HTMLSerializer[T]): String = {
    s"<html><body>${content.toHTML(serializer)}</body></html>"
  }

  def htmlSugar[T : HTMLSerializer](content: T): String = {
    val serializer = implicitly[HTMLSerializer[T]] // we send serializer explicitly
    s"<html><body>${content.toHTML(serializer)}</body></html>"
  }

  // implicitly

  case class Permissions(mask: String)
  implicit val defaultPermissions: Permissions = Permissions("0744")

  // in some other par of the code (implicit val => explicit val)
  val standardPerm = implicitly[Permissions]


}
