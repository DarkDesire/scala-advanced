package tech.eldarkaa.lectures.par2afp

object Monads extends App {

  // Monads are a kind of types which have some fundamental ops
  trait MonadTemplate[A]{
    def unit(value:A): MonadTemplate[A] // also called "pure" , "apply"
    def flatMap[B](f:A =>MonadTemplate[B]): MonadTemplate[B] // called "bind"
  }
  // List, Option, Try, Future, Stream, Set are all monads
  // Operations must satisfy the monad laws
  // 1. left-identity     | unit(x).flatMap(f) == f(x)
  // 2. right-identity    | aMonadInstance.flatMap(unit) == aMonadInstance
  // 3 associativity      | m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))
  // 1
  val f = (x:Int) => List(x+1)
  List(1).flatMap(f) == f(1)
  f(1) ++ Nil.flatMap(f) == f(1)
  // 2
  val aMonadInstance = List(1)
  aMonadInstance.flatMap(x => List.apply(x)) == aMonadInstance
  // 3
  val m = List(1, 2,3)
  val g = (x:Int) => List(x*3)
  m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))

  (f(1) ++ f(2) ++ f(3)).flatMap(g)
  f(1).flatMap(g) ++ f(2).flatMap(g) ++ f(3).flatMap(g)
  m.flatMap(f(_).flatMap(g))
  m.flatMap(x => f(x).flatMap(g))

  object Laws {
    /*
    left-identity

    unit.flatMap(f) = f(x)
    Attempt(x).flatMap(f) = f(x) // Success case!
    Success(x).flatMap(f) = f(x) // proved.
     */

    /*
    right-identity

    attempt.flatMap(unit) = attempt
    Success(x).flatMap(x => Attempt(x)) = Attempt(x) // Success case!
    Fail(_).flatMap(...) = Fail(e) // proved.
     */

    /*
    associativity
    attempt.flatMap(f).flatMap(g) = attempt.flatMap(x => f(x).flatMap(g))
    Fail(e).flatMap(f).flatMap(g) = Fail(e)
    Fail(e).flatMap(x => f(x).flatMap(g)) = Fail(e)

    Success(v).flatMap(f).flatMap(g) =
      f(v).flatMap(g) OR Fail(e)

    Success(v).flatMap( x=> f(x).flatMap(g)) =
      f(v).flatMap(g) OR Fail(e)
     */
  }

  val attempt = Attempt {
    throw new RuntimeException("my own monad, ouh yeah!")
  }
  println(attempt)

  /*
  Exercise.
  1. Implement: implement a Lazy[T] monad =
  computation which will only be executed when it's needed


  2. Monads = unit + flatMap
     Monads = unit + map + flatten

   */

}

// Our own Try monad
trait Attempt[+A] {
  def flatMap[B](f: A => Attempt[B]): Attempt[B]
}

object Attempt {
  def apply[A](a: => A): Attempt[A] =
    try {
      Success(a)
    } catch { case e: Throwable => Failure(e)}
}

case class Success[A](value: A) extends Attempt[A] {
  def flatMap[B](f: A => Attempt[B]): Attempt[B] =
    try {
      f(value)
    } catch { case e: Throwable => Failure(e)}
}

case class Failure(e: Throwable) extends Attempt[Nothing] {
  def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
}
