package tech.eldarkaa.lectures.part4implicits

object OrganizingImplicits extends App {

  implicit def reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  // implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ < _)
  println(List(1,4,5,3,2).sorted)
  // scala.Predef

  /*
    Implicits: (used as implicit parameters
      -var/var
      -object
      -accessor methods = defs with no parentheses
       (only like implicit def reverseOrdering)
   */

  // Exercise
  case class Person(name: String, age:Int)
  object Person{
    implicit val alphabeticPersonOrdering: Ordering[Person] = Ordering.fromLessThan( (pa, pb) => pa.name.compareTo(pb.name) < 0)
  }
  val persons = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66),
  )
  println(persons.sorted)

  /*
    Implicit scope: (priority)
      - normal scope = LOCAL SCOPE (same file)
      - imported scope
      - companions of all types involved in the signature
        - List
        - Ordering
        - all types involved in the signature
   */
  // def sorted[B >: A](implicit ord: Ordering[B]): List[B]



  // BEST PRACTICES:

  // when you defining an implicit val:
  // #1
  // if there is a single possible value for it
  // and you can edit the code for the type
  // then define the implicit in the - COMPANION

  // #2
  // is there is are many possible values for it
  // but a single GOOD one
  // and you can edit the code for the type
  // and you can edit the code for the type
  // then define the GOOD implicit in the - COMPANION



  object AlphabeticNameOrdering{
    implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan( (pa, pb) => pa.name.compareTo(pb.name) < 0)
  }

  object AgeOrdering{
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan( (pa, pb) => pa.age < pb.age )
  }
  import AgeOrdering._
  println(persons.sorted)

  /*
    Exercise

    - totalPrice = most used (50%)
    - by unit count (25%)
    - by unit price (25%)
   */
  case class Purchase(nUnits:Int, unitPrice:Double)
  object Purchase {
    implicit val totalPriceOrdering: Ordering[Purchase] =
      Ordering.fromLessThan( (pa, pb) => pa.nUnits*pa.unitPrice < pb.nUnits*pb.unitPrice )
  }

  object UnitCountOrdering {
    implicit val unitCountOrdering: Ordering[Purchase] =
      Ordering.fromLessThan( (pa, pb) => pa.nUnits < pb.nUnits )
  }
  object UnitPriceOrdering {
    implicit val unitPriceOrdering: Ordering[Purchase] =
      Ordering.fromLessThan( (pa, pb) => pa.unitPrice < pb.unitPrice )
  }

  val purchases = List(
    Purchase(1, 2d),
    Purchase(3, 4d),
    Purchase(5, 6d),
    Purchase(1, 10d),
    Purchase(10, 1d),
  )
  println(s"total: ${purchases.sorted}")
  println(s"unit count: ${purchases.sorted(UnitCountOrdering.unitCountOrdering)}")
  println(s"unit price: ${purchases.sorted(UnitPriceOrdering.unitPriceOrdering)}")
}
