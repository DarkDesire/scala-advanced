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

}
