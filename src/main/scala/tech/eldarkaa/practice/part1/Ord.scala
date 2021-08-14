package tech.eldarkaa.practice.part1

import tech.eldarkaa.practice.part1.Ord.Compare
import tech.eldarkaa.practice.part1.Ord.Compare.{EQ, GT, LT}


/*
  - HOMEWORK
 */
trait Ord[T] extends Eq[T] {
  def compare(x: T, y: T): Compare

  def ===(x: T, y: T): Boolean = {
    compare(x, y) match {
      case Compare.EQ => true
      case _ => false
    }
  }
}

object Ord {
  sealed trait Compare

  object Compare {
    case object LT extends Compare // less than
    case object EQ extends Compare // equals to
    case object GT extends Compare // greater than
  }

  def compareFromInt(value: Int): Compare = {
    if (value == 0) EQ
    else if (value > 0) GT else LT
  }

  implicit def ordLists[T: Ord](implicit ord: Ord[T]): Ord[List[T]] =
    (x: List[T], y: List[T]) =>
      x.zip(y).collectFirst { case (x, y) if ord.compare(x, y) != Compare.EQ => ord.compare(x, y) }
        .getOrElse {
          (x.size, y.size) match {
            case (xSize, ySize) if xSize > ySize => GT
            case (xSize, ySize) if xSize < ySize => LT
            case (xSize, ySize) if xSize == ySize => EQ
          }
        }
}