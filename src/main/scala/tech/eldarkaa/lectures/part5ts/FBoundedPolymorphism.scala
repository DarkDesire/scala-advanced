package tech.eldarkaa.lectures.part5ts

object FBoundedPolymorphism extends App {



  /*trait Animal {
    def breed: List[Animal]
  }

  class Cat extends Animal {
    override def breed: List[Animal] = ??? // List[Cat]
  }
  class Dog extends Animal {
    def breed: List[Animal] = ??? // List [Dog]
  }*/

  ////////

  // solution 1 - naive
  /*trait Animal {
    def breed: List[Animal]
  }

  class Cat extends Animal {
    override def breed: List[Cat] = ??? // List[Cat]
  }
  class Dog extends Animal {
    def breed: List[Cat] = ??? // List [Dog]
  }*/

  ////////

  // solution 2 - FBP
  /*trait Animal[A <: Animal[A]] { // recursive type: F-bounded polymorphism
    def breed: List[Animal[A]]
  }

  class Cat extends Animal[Cat] {
    override def breed: List[Animal[Cat]] = ??? // List[Cat]
  }
  class Dog extends Animal[Dog] {
    def breed: List[Animal[Dog]] = ??? // List [Dog]
  }

  trait Entity[A <: Entity[A]] // ORM
  class Person extends Comparable[Person] { // FBP
    def compareTo(o: Person): Int = ???
  }

  // another problem...
  class Crocodile extends Animal[Dog] {
    def breed: List[Animal[Dog]] = ???
  }*/

  ////////

  // solution 3 - FBP + self-types
  /*trait Animal[A <: Animal[A]] { self: A => // recursive type: F-bounded polymorphism
    def breed: List[Animal[A]]
  }

  class Cat extends Animal[Cat] {
    override def breed: List[Animal[Cat]] = ??? // List[Cat]
  }
  class Dog extends Animal[Dog] {
    def breed: List[Animal[Dog]] = ??? // List [Dog]
  }
  /*class Crocodile extends Animal[Dog] { // self-type Crocodile dosn't conform to A
    def breed: List[Animal[Dog]] = ???
  }*/


  // limitation
  trait Fish extends Animal[Fish]
  class Shark extends Fish {
    def breed: List[Animal[Fish]] = List(new Cod)
  }

  class Cod extends Fish {
    def breed: List[Animal[Fish]] = ???
  }
*/

  // solution 4 - type-class
  /*trait Animal
  trait CanBreed[A] {
    def breed(a: A): List[A]
  }
  class Dog extends Animal
  class Cat extends Animal
  object Dog {
    implicit object DogsCanBreed extends CanBreed[Dog] {
      def breed(a: Dog): List[Dog] = List()
    }
  }
  object Cat {
    implicit object CatsCanBreed extends CanBreed[Cat] {
      def breed(a: Cat): List[Cat] = List()
    }
  }

  implicit class CanBreedOps[A](animal: A) {
    def breed(implicit canBreed: CanBreed[A]): List[A] = canBreed.breed(animal)
  }
  val dog = new Dog
  dog.breed

  val cat = new Dog
  cat.breed*/


  // solution #5
  trait Animal[A] { // pure type classes
    def breed(a: A): List[A]
  }
  class Dog
  object Dog {
    implicit object DogAnimal extends Animal[Dog]{
      def breed(a: Dog): List[Dog] = List()
    }
  }
  class Cat
  object Cat {
    implicit object CatAnimal extends Animal[Cat]{
      def breed(a: Cat): List[Cat] = List()
    }
  }
  implicit class AnimalOps[A](animal: A){
    def breed(implicit animalTypeClassInstance: Animal[A]): List[A] =
      animalTypeClassInstance.breed(animal)
  }

  val dog = new Dog
  dog.breed
}
