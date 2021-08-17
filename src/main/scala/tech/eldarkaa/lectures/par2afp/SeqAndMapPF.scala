package tech.eldarkaa.lectures.par2afp

object SeqAndMapPF extends App {

  // Seqs are partially defined on the domain [0, ..., length -1]
  // Seqs are Partial Functions!
  trait Seq[+A] extends PartialFunction[Int, String] {
    def apply(value: Int): String
  }

  val numbers = List(1,2,3)
  numbers(1) // 2
  // numbers(3) // java.lang.IndexOutOfBoundException


  // Map is defined on domain of its keys
  // Seqs are Partial Functions!
  trait Map[A,+B] extends PartialFunction[A,B] {
    def apply(key: A): B
    def get(key:A): Option[B]
  }

  val phoneMappings = Map(
    2 -> "ABC", 3 -> "DEF"
  )
  phoneMappings(2) // abc
  // phoneMappings(3) // java.lang.NoSuchElementException


}
