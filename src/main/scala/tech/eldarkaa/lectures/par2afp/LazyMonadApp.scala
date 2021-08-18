package tech.eldarkaa.lectures.par2afp

object LazyMonadApp extends App {
  val lazyInstance = Lazy {
    println("Today I don't feel like doing anything")
    42
  }

  val flatMappedInstance = lazyInstance.flatMap(x => Lazy{10*x})
  val flatMappedInstance2 = lazyInstance.flatMap(x => Lazy{10*x})
  flatMappedInstance.use()
  flatMappedInstance2.use()
}
class Lazy[+A](value: => A) {
  // call by need
  private lazy val internalValue = value
  def use(): A = internalValue
  def flatMap[B](f: (=>A) => Lazy[B]): Lazy[B] = f(internalValue)
}
object Lazy {
  def apply[A](value: => A): Lazy[A] = new Lazy(value)
}

/*
  left-identity
  unit.flatMap(f) == f(v)
  Lazy(v).flatMap(f) == f(v)

  right-identity
  l.flatMap(unit) = l
  Lazy(v).flatMap(x => Lazy(x)) == Lazy(v)

  associativity
  Lazy(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
  Lazy(v).flatMap(x= > f(x).flatMap(g)) = f(v).flatMap(g)
 */