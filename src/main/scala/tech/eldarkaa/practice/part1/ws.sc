import tech.eldarkaa.practice.part1.Ratio
import tech.eldarkaa.practice.part1.syntax.ord._
import tech.eldarkaa.practice.part1.syntax.eq._

Ratio(1,2) === Ratio(1,2)

val list1 = List(
  Ratio(1,2),
  Ratio(2,2))
val list2 = List(
  Ratio(1,5),
  Ratio(2,3))
val list3 = List(
  Ratio(1,2),
  Ratio(2,2))
val list4 = List(
  Ratio(1,2),
  Ratio(1,2))
val list5 = List(
  Ratio(1,2),
  Ratio(1,2),
  Ratio(1,2))

list1 === list2
list1 === list3

// Ratio(1,2) compare Ratio(1,2)
val x = new Ratio(3,2)
val y = new Ratio(1,2)

x compare y // 3/2 vs 1/2 // GT

list1 compare list2 // 3/2 vs 1/5+2/3 = 5/15 = 1/3 // GT
list1 compare list3 // 3/2 vs 3/2 // EQ
list4 compare list5 // size2 vs size3 // LT