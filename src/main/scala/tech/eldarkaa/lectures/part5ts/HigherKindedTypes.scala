package tech.eldarkaa.lectures.part5ts

import scala.concurrent.Future

object HigherKindedTypes extends App {

  trait AHigherKindedType[F[_]]

  trait MyList[T] {
    def flatMap[B](f: T=>MyList[B]): MyList[B]
  }
  trait MyFuture[T] {
    def flatMap[B](f: T=>MyList[B]): MyFuture[B]
  }
  trait MyOption[T] {
    def flatMap[B](f: T=>MyList[B]): MyOption[B]
  }


  // combine / multiply List(1,2) * List("a", "b") => List(1a, 1b, 2a, 2b)
/*

  def multiply[A,B](listA: List[A], listB: List[B]): List[(A,B)] =
    for {
      a <- listA
      b <- listB
    } yield (a,b)

  def multiply[A,B](listA: Option[A], listB: Option[B]): Option[(A,B)] =
    for {
      a <- listA
      b <- listB
    } yield (a,b)
*/


  trait Monad[F[_], A] { // higher-kinded type class
    def flatMap[B](f: A => F[B]): F[B]
    def map[B](f: A=> B): F[B]
  }

  implicit class MonadListOps[A](list: List[A]) extends Monad[List, A] {
    def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)
    def map[B](f: A => B): List[B] = list.map(f)
  }
  implicit class OptionListOps[A](option: Option[A]) extends Monad[Option, A] {
    def flatMap[B](f: A => Option[B]): Option[B] = option.flatMap(f)
    def map[B](f: A => B): Option[B] = option.map(f)
  }

  val monadList = new MonadListOps(List(1,2,3))
  monadList.flatMap(x => List(x, x+1))
  // from Monad[List, Int] = List[Int]
  monadList.map(_ * 2) // List Int
  // from Monad[List, Int] = List[Int]

  def multiply[F[_],A,B](implicit monadA: Monad[F, A], monadB: Monad[F,B]): F[(A,B)] = {
    for {
      a <- monadA
      b <- monadB
    } yield (a,b)
  }


  println(
    multiply(List(1,2), List("a","b")),
    multiply(List(1,2), List(Some(3), None))
  )
}
