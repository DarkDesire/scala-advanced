package tech.eldarkaa.practice.part1

import tech.eldarkaa.practice.part1.Ord.Compare

object syntax {
  object eq {
    implicit class EqOps[T](val x:T) extends AnyVal{
      def ===(y:T)(implicit eq:Eq[T]):Boolean = eq.===(x,y)
    }
  }

  object ord {
    implicit class OrdOps[T](val x:T) extends AnyVal{
      def compare(y:T)(implicit ord:Ord[T]): Compare = ord.compare(x,y)
    }
  }
}
