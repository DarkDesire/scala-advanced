package tech.eldarkaa.lectures.part4implicits

import java.util.Date

object JSONSerialization extends App {

  /*
    Users, posts, feeds
    Serialize to JSON
   */

  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])

  /*
   1 - intermediate data types: [String, Int, List, Date]
   2 - type class for conversion (case classes -> to intermediate data types)
   3 - serialize to JSON
   */
  // 1
  sealed trait JSONValue { // intermediate data type, start hierarchy
    def stringify: String
  }
  final case class JSONString(value: String) extends JSONValue{
    def stringify: String = "\""+value+"\""
  }
  final case class JSONArray(values: List[JSONValue]) extends JSONValue{
    def stringify: String = values.map(_.stringify).mkString("[",",","]")
  }
  final case class JSONNumber(value: Int) extends JSONValue{
    def stringify: String = value.toString
  }
  final case class JSONObject(values: Map[String, JSONValue]) extends JSONValue{
    /*
     {
      "name": "John",
      "age": 22,
      "friends": ["","",...]
      "latestPost": {
        "content": "Scala rocks"
      }
     }
     */
    def stringify: String = values.map {
      case (key, value) => "\"" + key + "\":" + value.stringify
    }.mkString("{", ",", "}")
  }

  val data = JSONObject(Map(
    "user" -> JSONString("Daniel"),
    "posts" -> JSONArray(List(
      JSONString("Scala rocks!"),
      JSONNumber(453)
    ))
  ))

  println(data.stringify)

  // 2 . type class
  /*
    2.1 - type class
    2.2 - type class instances (implicit object..)
    2.3 - conversion
   */
  // 2.1
  trait JSONConverter[T]{
    def convert(value:T): JSONValue
  }
  // 2.2
  // existing data types
  implicit object StringConverter extends JSONConverter[String]{
    def convert(value: String): JSONValue = JSONString(value)
  }
  implicit object NumberConverter extends JSONConverter[Int]{
    def convert(value: Int): JSONValue = JSONNumber(value)
  }

  // 2.3
  implicit class JSONOps[T](value: T) {
    def toJSON(implicit converter: JSONConverter[T]): JSONValue = converter.convert(value)
  }
  // custom data types converters
  implicit object UserConverter extends JSONConverter[User]{
    override def convert(user: User): JSONValue = JSONObject(Map(
      "name" -> JSONString(user.name),
      "age" -> JSONNumber(user.age),
      "email" -> JSONString(user.email)
    ))
  }
  implicit object PostConverter extends JSONConverter[Post]{
    override def convert(post: Post): JSONValue = JSONObject(Map(
      "content" -> JSONString(post.content),
      "createdAt" -> JSONString(post.createdAt.toString)
    ))
  }
  implicit object FeedConverter extends JSONConverter[Feed]{
    override def convert(feed: Feed): JSONValue = JSONObject(Map(
      "user" -> feed.user.toJSON,
      "posts" -> JSONArray(feed.posts.map(_.toJSON))
    ))
  }
  // call stringify on result
  val now = new Date(System.currentTimeMillis())
  val john = User("John", 42, "john@rockthejvm.com")
  val feed = Feed(john, List(
    Post("Hello, Scala", now),
    Post("Look at this cute puppy", new Date(2021, 0, 1))
  ))

  println(feed.toJSON.stringify)
}
