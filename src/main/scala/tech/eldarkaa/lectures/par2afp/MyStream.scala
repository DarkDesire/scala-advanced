package tech.eldarkaa.lectures.par2afp

import java.util.NoSuchElementException
import scala.annotation.tailrec

/*
  Exercise.
  Implement a lazily evaluated, single linked STREAM of elements
 */

object MyStream {
  def from[A](start: A)(generator: A => A) : MyStream[A] =
    new LazyStream[A](start, MyStream.from(generator(start))(generator))
}

abstract class MyStream[+A] {
  def isEmpty: Boolean
  def head: A
  def tail: MyStream[A]

  def #::[B >: A](element:B): MyStream[B] // prepand operator
  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] // concat two streams

  def foreach(f: A => Unit): Unit
  def map[B](f: A=>B): MyStream[B]
  def flatMap[B](f: A=>MyStream[B]): MyStream[B]
  def filter(p: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A] // take the first n elements out of the stream
  def takesAsList(n: Int): List[A] = take(n).toList()

  /*
   [1,2,3].toList([]) =
   [2,3].toList([1]) =
   [3].toList([2,1]) =
   [].toList([3,2,3]) =
   [1,2,3] (reverse)
   */
  @tailrec
  final def toList[B >: A](acc: List[B] = Nil): List[B] =
    if (isEmpty) acc.reverse
    else tail.toList(head :: acc)

}

class LazyStream[A](hd: A, tl: => MyStream[A]) extends MyStream[A]{
  def isEmpty: Boolean = false
  override val head: A = hd
  override lazy val tail: MyStream[A] = tl
  // call by name + lazy val = CALL BY NEED

  // evaluated by need
  def #::[B >: A](element: B): MyStream[B] = new LazyStream[B](element, this)
  // evaluated by need
  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] =
    new LazyStream[B](head, tail ++ anotherStream)

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }

  // evaluated by need (map, flatMap, filter)
  def map[B](f: A => B): MyStream[B] = new LazyStream[B](f(head), tl.map(f))
  def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tl.flatMap(f)
  def filter(p: A => Boolean): MyStream[A] =
    if (p(head)) new LazyStream(head, tail.filter(p))
    else tail.filter(p)

  def take(n: Int): MyStream[A] =
    if (n <= 0) EmptyLazyStream
    else if (n == 1) new LazyStream(head, EmptyLazyStream)
    else new LazyStream(head, tail.take(n-1))
}
object EmptyLazyStream extends MyStream[Nothing]{
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException
  def tail: MyStream[Nothing] = throw new NoSuchElementException

  def #::[B >: Nothing](element: B): MyStream[B] = new LazyStream[B](element, this)
  def ++[B >: Nothing](anotherStream: => MyStream[B]): MyStream[B] = anotherStream

  def foreach(f: Nothing => Unit): Unit = ()
  def map[B](f: Nothing => B): MyStream[B] = this
  def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this
  def filter(p: Nothing => Boolean): MyStream[Nothing] = this

  def take(n: Int): MyStream[Nothing] = this
}

object StreamsPlayground extends App {
  val naturals = MyStream.from(1)(x => x+1)
  //println(naturals.head)
  //println(naturals.tail.head)
  //println(naturals.tail.tail.head)

  val startFrom0 = 0 #:: naturals // naturals.#::(0)
  //println(startFrom0.head)
  //println(startFrom0.tail.head)

  //startFrom0.take(10000).foreach(println)
  // map
  //startFrom0.map(_*2).take(100).foreach(println)
  //println(startFrom0.flatMap(x => new LazyStream(x, new LazyStream(x+1, EmptyLazyStream))).take(10).toList())
  println(startFrom0.filter(_ < 10).take(10).take(20).toList())

  // Exercises on streams
  // 1 - stream of fibonacci numbers
  val stream = new LazyStream(0, MyStream.from(1)(x => x+1))
  // 2 - stream of prime numbers with Eratosthenes' sieve
  /*
    [ 2 3 4 .. ]
    filter out all numbers divisible by 2
    [ 2 3 5 7 9 11 ... ]
    filter out all numbers divisible by 3
    [ 2 3 5 7 11 13 ... ]
    filter out all numbers divisible by 5
    [ 2 3 5 7 11 13 ... ]
   */
  // 1
  def fibonacci(first:BigInt, second:BigInt): MyStream[BigInt] =
    new LazyStream[BigInt](first, fibonacci(second, first+second))

  println(fibonacci(0,1).take(100).toList())

  // 2
  /*
    [2,3,4,5,6,7,8,9,10,11,12]
   */
  def eratosthenes(numbers: MyStream[Int]): MyStream[Int] =
    if (numbers.isEmpty) numbers // if finite or it's end
    else {
      new LazyStream[Int](numbers.head, eratosthenes(numbers.tail
        .filter(_ % numbers.head != 0))
      )
    }

  println(eratosthenes(MyStream.from(2)(_ + 1)).take(100).toList())
}