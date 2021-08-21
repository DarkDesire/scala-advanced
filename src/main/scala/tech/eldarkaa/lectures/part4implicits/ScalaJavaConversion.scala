package tech.eldarkaa.lectures.part4implicits

import java.{util => ju}
object ScalaJavaConversion extends App {

  import scala.jdk.CollectionConverters._
  val javaSet: ju.Set[Int] = new ju.HashSet[Int]()
  (1 to 5).foreach(javaSet.add)
  println(javaSet)

  val scalaSet = javaSet.asScala

  /*
  Iterator
  Iterable
  ju.List - collection.mutable.Buffer
  ju.Set - collection.mutable.Set
  ju.Map - collection.mutable.Map
   */

  import collection.mutable._
  val numbersBuffer = ArrayBuffer[Int](1,2,3)
  val juNumbersBuffer = numbersBuffer.asJava

  println(juNumbersBuffer.asScala eq numbersBuffer)

  val numbers = List(1,2,3) // immutable
  val juNumbers = numbers.asJava // ju.List (suppose to be immutable)
  val backToScala = juNumbers.asScala // mutable.Buffer
  println(backToScala eq numbers) // false
  println(backToScala == numbers) // true

  // juNumbers.add(7) // throw error

  /*
  Exercise.
  Crate a Scala-Java Optional-Option
   */
  class ToScala[T](value: => T){
    def asScala: T = value
  }
  implicit def asScalaOption[T](o: ju.Optional[T]):
    ToScala[Option[T]] = new ToScala[Option[T]](
      if (o.isPresent) Some(o.get)else None
    )

  class ToJava[T](value: => T){
    def asJava: T = value
  }
  implicit def asJavaOptional[T](o: Option[T]):
  ToJava[ju.Optional[T]] = new ToJava[ju.Optional[T]](
    if (o.isDefined) ju.Optional.of(o.get) else ju.Optional.empty[T]
  )

  val juOptional: ju.Optional[Int] = ju.Optional.of(2)
  val scalaOption = juOptional.asScala
  println(scalaOption)

  val scOption = Some(2)
  val javaOptional = scOption.asJava
  println(javaOptional)
}
