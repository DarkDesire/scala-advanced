package tech.eldarkaa.practice.part1

trait Eq[T] {
  def ===(x: T, y: T): Boolean
}
object Eq {
  implicit def compareLists[T](implicit eq: Eq[T]): Eq[List[T]] =
    (x: List[T], y: List[T]) => x.size == y.size && x.zip(y).forall { case (x, y) => eq.===(x,y) }
}

