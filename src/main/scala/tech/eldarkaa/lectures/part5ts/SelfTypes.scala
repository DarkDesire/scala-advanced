package tech.eldarkaa.lectures.part5ts

object SelfTypes extends App {

  // self-type vs inheritance
  class A
  class B extends A // B is an A

  trait T
  trait S {self: T =>} // S requires T

  ////////////

  // requiring a type to be mixed in
  trait Instrumentalist {
    def play(): Unit
  }

  trait Singer { self: Instrumentalist => // SELF TYPE:  whoever implements Singer
    def sing(): Unit
  }

  class LeadSinger extends Singer with Instrumentalist {
    override def sing(): Unit = ???
    override def play(): Unit = ???
  }

  // not allowed
  /*class Vocalist extends Singer {
    def sing(): Unit = ???
  }*/

  // also ok
  val jamesHetfield = new Singer with Instrumentalist {
    def sing(): Unit = ???
    def play(): Unit = ???
  }

  class Guitarist extends Instrumentalist {
    def play(): Unit = println("(guitar solo)")
  }
  val ericClapton = new Guitarist with Singer {
    def sing(): Unit = ???
  }

  // CAKE PATTERN (check types on compile) = "dependency injection" (check types in runtime)

  class Component {
    // API
  }
  class ComponentA extends Component
  class ComponentB extends Component
  class DependentComponent(val component: Component)

  // CAKE PATTERN (check types on compile)
  trait ScalaComponent {
    // API
    def action(x:Int): String
  }
  trait ScalaDependentComponent { self: ScalaComponent =>
    def dependentAction(x: Int): String = action(x) + " this rocks"
  }
  trait ScalaApplication { self: ScalaDependentComponent => }

  // layer 1 - small components
  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent
  // layer 2 - compose
  trait Profile extends ScalaDependentComponent with Picture
  trait Analytics extends ScalaDependentComponent with Stats
  // layer 3 - app
  trait AnalyticsApp extends ScalaApplication with Analytics


  // cyclical dependencies
  // class X extends Y
  // class Y extends X

  trait X {self: Y =>}
  trait Y {self: X =>}
}
