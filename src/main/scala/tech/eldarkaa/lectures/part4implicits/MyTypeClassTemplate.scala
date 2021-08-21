package tech.eldarkaa.lectures.part4implicits

// TYPE CLASS - trait[T]
// object = TypeClass[T] - type class instances
// we instantiate them only ONCE

trait MyTypeClassTemplate[T] {
  def action(value: T):String
}
object MyTypeClassTemplate {
  def apply[T](implicit instance: MyTypeClassTemplate[T]):MyTypeClassTemplate[T] = instance
}
