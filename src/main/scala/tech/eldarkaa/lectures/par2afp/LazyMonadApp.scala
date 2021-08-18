package tech.eldarkaa.lectures.par2afp

object LazyMonadApp extends App {
  val lazyInstance = Lazy {
    println("Today I don't feel like doing anything")
    42
  }
  println(lazyInstance.use())
}
class Lazy[+A](value: => A) {
  def use(): A = value
  def flatMap[B](f: A => Lazy[B]): Lazy[B] = f(value)
}
object Lazy {
  def apply[A](value: => A): Lazy[A] = new Lazy(value)
}