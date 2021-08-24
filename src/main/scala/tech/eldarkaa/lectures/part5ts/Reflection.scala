package tech.eldarkaa.lectures.part5ts

object Reflection extends App {

  // reflection + macros + quasiquotes
  // => METAPROGRAMMING

  case class Person(name: String){
    def sayMyName(): Unit = println(s"My name is $name")
  }

  // 0 - import
  import scala.reflect.runtime.{universe => ru}

  // 1 - MIRROR
  val m = ru.runtimeMirror(getClass.getClassLoader)

  // 2 - create a class object (symbol) = "description" = blueprint
  val clazz: ru.ClassSymbol = m.staticClass("tech.eldarkaa.lectures.part5ts.Reflection.Person")
  // creating a class object by name

  // 3 - create a reflected mirror (mirror) = "can do things"
  val cm = m.reflectClass(clazz)

  // 4 - get the constructor (symbol)
  val constructor: ru.MethodSymbol = clazz.primaryConstructor.asMethod
  // 5 - reflect the constructor (MethodMirror)
  val constructorMirror = cm.reflectConstructor(constructor)
  // 6 - invoke the constructor
  val instance = constructorMirror.apply("John")

  println(instance)

  ////////////////////////////////////

  // I have an instance
  val p = Person("Mary") // from the wire as a serialized object
  // method name computed from somewhere else
  val methodName = "sayMyName"

  // 1 - MIRROR
  // 2 - reflect the instance (mirror)
  val reflected = m.reflect(p)
  // 3 - method symbol (symbol)
  val methodSymbol = ru.typeOf[Person].decl(ru.TermName(methodName)).asMethod
  // 4 - reflect the method (MethodMirror)
  val method = reflected.reflectMethod(methodSymbol)
  // 5 - invoke the method

  method.apply()

  // type erasure
  // pp #1: different types at runtime
  val numbers = List(1,2,3)
  numbers match {
    case listOfStrings: List[String] => println("list of strings")
    case listOfInts: List[Int] => println("list of ints")
    case _ =>
  }

  // pp #2: limitations on overloads
  // def processList(list: List[Int]): Int = 43
  // def processList(list: List[String]): Int = 43

  // reflection workaround

  // TypeTags
  // 0 - import
  import ru._

  // 1 - create a type tag "manually"
  val ttag: ru.TypeTag[Person] = typeTag[Person]
  println(ttag.tpe)

  class MyMap[K, V]
  // 2 -pass type tags as implicit params
  def getTypeArguments[T](value: T)(implicit typeTag: TypeTag[T]): List[Type] = typeTag.tpe match {
    case TypeRef(pre, sym, args) => args
    case _ => List()
  }

  val myMap = new MyMap[Int, String]
  val typeArgs = getTypeArguments(myMap) // TypeTag[MyMap[Int,String]]
  println(typeArgs)

  def isSubtype[A,B](implicit ttA: TypeTag[A], ttB: TypeTag[B]): Boolean = {
    ttA.tpe <:< ttB.tpe
  }

  class Animal
  class Dog extends Animal
  println(isSubtype[Dog, Animal])

  ////// Marry sayMyName
  // 3 - method symbol (symbol)
  val anotherMethodSymbol = typeTag[Person].tpe.decl(ru.TermName(methodName)).asMethod
  // 4 - reflect the method (MethodMirror)
  val anotherMethod = reflected.reflectMethod(anotherMethodSymbol)
  // 5 - invoke the method

  anotherMethod.apply()
}
