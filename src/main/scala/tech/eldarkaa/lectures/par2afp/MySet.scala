package tech.eldarkaa.lectures.par2afp

import scala.annotation.tailrec

object MySet{
  /*
  val s = MySet(1,2,3) = buildSet(seq(1,2,3), [])
  ? no => buildSet(seq(2,3), [] + 1)
  ? no => buildSet(seq(3), [1] + 2)
  ? no => buildSet(seq(), [1,2] + 3)
  [1,2,3]
   */
  def apply[A](values: A*): MySet[A] = {
    @tailrec
    def buildSet(valSeq: Seq[A], acc:MySet[A]): MySet[A] = {
      if (valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)
    }
    buildSet(values, new EmptySet[A])
   }
}

trait MySet[A] extends (A=>Boolean){
  /*
  Implement a functional set
  - apply
   */
  def apply(elem: A): Boolean = contains(elem)
  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A] // union

  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(p: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit

  /*
  Exercise.
  - remove elem
  - intersection with another set ((x))
  - difference with another set (x()x)
   */
  def -(elem:A): MySet[A] // remove
  def --(anotherSet: MySet[A]): MySet[A]  // difference
  def &(anotherSet: MySet[A]): MySet[A] // intersection
  def unary_! : MySet[A]
}
class PropertyBasedSet[A](property: A => Boolean) extends MySet[A] {
  def contains(elem: A): Boolean = property(elem)

  def +(elem: A): MySet[A] =
    new PropertyBasedSet[A](x => property(x) || x == elem)

  def ++(anotherSet: MySet[A]): MySet[A] =
    new PropertyBasedSet[A](x => property(x) || anotherSet(x))

  def map[B](f: A => B): MySet[B] = politelyFail
  def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail
  def foreach(f: A => Unit): Unit = politelyFail

  def filter(p: A => Boolean): MySet[A] =
    new PropertyBasedSet[A](x => property(x) && p(x))
  def -(elem:A): MySet[A] = filter(x => x != elem )// remove
  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)// difference
  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet) // intersection
  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !property(x))

  def politelyFail = throw new IllegalArgumentException("Really deep rabbit hole!")

}
class EmptySet[A] extends MySet[A] {
  def contains(elem: A): Boolean = false
  def +(elem: A): MySet[A] = new ASet[A](elem, this)
  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet
  def map[B](f: A => B): MySet[B] = new EmptySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  def filter(p: A => Boolean): MySet[A] = this
  def foreach(f: A => Unit): Unit = ()
  // part2
  def -(elem:A): MySet[A] = this
  def --(anotherSet: MySet[A]): MySet[A] = this
  def &(anotherSet: MySet[A]): MySet[A] = this
  def unary_! : MySet[A] = new PropertyBasedSet[A](_ => true)
}
class ASet[A](head: A, tail: MySet[A]) extends MySet[A] {
  def contains(elem: A): Boolean =
    elem == head || tail.contains(elem)
  def +(elem: A): MySet[A] =
    if (this.contains(elem)) this
    else new ASet(elem, this)

  /*
    (1,2,3) ++ (4,5) =
    (2,3) ++ (4,5) + 1
    = (3) ++ (4,5) + 1 + 2
    = () ++ (4,5) + 1 + 2 + 3
    = (4,5) + 1 + 2 + 3
    = (4, 5, 1, 2, 3)
   */
  def ++(anotherSet: MySet[A]): MySet[A] =
    tail ++ anotherSet + head
  def map[B](f: A => B): MySet[B] =
    tail.map(f) + f(head)

  def flatMap[B](f: A => MySet[B]): MySet[B] =
    tail.flatMap(f) ++ f(head)

  def filter(p: A => Boolean): MySet[A] = {
    val filteredTail = tail.filter(p)
    if (p(head)) filteredTail + head
    else filteredTail
  }
  def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }

  // part2
  def -(elem:A): MySet[A] =
    if (elem == head) tail
    else tail - elem + head

  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet(_))
  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet) // ! intersection = filtering

  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))
}

object MySetPlayground extends App {
  val s = MySet(1,2,3,4)
  s +5 ++ MySet(-1,-2, 4) + 3 flatMap (x => MySet(x, x*10)) filter(_%2==0) foreach println

  val negative = !s // s.unary_! = all the naturals not equal to 1,2,3,4
  println(negative(2))
  println(negative(5))

  val negativeEven = negative.filter(_ % 2 == 0)
  println(negativeEven(5))

  val negativeEven5 = negativeEven + 5 // all the even numbers > 4+5
  println(negativeEven5(5))
}

