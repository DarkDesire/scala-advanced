package tech.eldarkaa.lectures.part5ts

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  // what is variance ?
  // "inheritance" - type substitution of generics


  class Cage[T]
  // yes - covariance
  class CCage[+T]
  val ccage: CCage[Animal] = new CCage[Cat]

  // no - invariance
  class ICage[T]
  // wrooong
  // val icage: ICage[Animal] = new ICage[Cat]
  // val x: Int = "hello"

  // hell no - oposite - contravariance
  class XCage[-T]
  val contraCage: XCage[Cat] = new XCage[Animal]


  class InvariantCage[T](val animal: T) // ok
  class InvariantVariableCage[T](var animal: T) // ok

  // covariant positions
  class CovariantCage[+T](val animal: T) //ok  // val animal in COVARIANT POSITION / PLACES
  //class CovariantVariableCage[+T](var animal: T) // var animal in CONTRAVARIANT POSITION
  /*
    val ccage: CCage[Animal] = new XCage[Cat](new Cat)
    ccage.animal = new Crocodile
   */


  // class ContravariantCage[-T](val animal: T) // val animal occurs in COVARIANT POSITION
  // class ContravariantVariableCage[-T](var animal:T) // var animal in COVARIANT POSITION
  /*
    vat catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
   */


  /*trait AnotherCovariantCage[+T] {
    def addAnimal(animal: T) // CONTRAVARIANT position
  }*/
  // not compile because we would be able to write like:
  /*
    val ccage: CCage[Animal] = new CCage[Dog]
    ccage.add(new Cat)
   */

  class AnotherContravariantCage[-T]{
    def addAnimal(animal: T): Boolean = true
  }
  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  // not allowed acc.addAnimal(new Dog)
  class Kitty extends Cat
  acc.addAnimal(new Kitty)

  class MyList[+A]{
    def add[B >: A](element: B): MyList[B] = new MyList[B] // widening the type

  }

  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals: MyList[Cat] = animals.add(new Cat)
  val evenMoreAnimals: MyList[Animal] = moreAnimals.add(new Dog) // widening the type

  // METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITION

  // return types
  class PetShop[-T] {
    //def get(isItAPuppy: Boolean): T // method return types are in COVARIANT POSITION
    /*
      val catShop = new PetShop[Animal]
      def get(isItAPuppy: Boolean): Animal = new Cat

      val dogShop: PetShop[Dog] = catShop
      def get(isItAPuppy: Boolean): Animal = new Cat // EVIL CATS
     */
    // hack
    def get[S <: T](isItAPuppy: Boolean, defaultAnimal: S): S = defaultAnimal

  }

  val shop: PetShop[Dog] = new PetShop[Animal]
  // val evilCat = shop.get(true, new Cat)
  // inferred type arguments CAT
  // do not conform to method
  // get's type parameter bounds [S <: tech.eldarkaa.lectures.part5ts.Variance.Dog]

  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova)


  /*
        BIG RULE
        - method arguments are in CONTRAVARIANT POSITION
        - return types are in COVARIANT POSITION
   */


  /* Variance Exercise
    1. Invariant, covariant, contravariant Parking[T](things:List[T]){
      park(vehicle: T)
      impound(vehicles: List[T])
      checkVehicles(condition: String): List[T]
    }
    2. used someone else's API: IList[T] (invariant)
    3. Parking = monad!
      - flatMap
   */
  class Vehicle
  class Bike extends Vehicle
  class Cars extends Vehicle
  class Moto extends Vehicle

  class IList[T]
  // 1.1 Invariant
  // allow 1 type and only 1
  class IParking[T](vehicles: List[T]) {
    def park(vehicle: T): IParking[T] = new IParking(vehicle :: vehicles)
    def impound(vehicles: List[T]): IParking[T] = new IParking(this.vehicles.filter(v => !vehicles.contains(v)))
    def checkVehicles(condition: Boolean): List[T] = vehicles
    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }
  // 1.2 Covariant
  class CParking[+T](vehicles: List[T]) {
    def park[V >: T](vehicle: V): CParking[V] = new CParking(vehicle :: vehicles)
    def impound[V >: T](vehicles: List[V]): CParking[V] = new CParking(this.vehicles.filter(v => !vehicles.contains(v)))
    def checkVehicles(condition: Boolean): List[T] =  vehicles
    def flatMap[S](f: T => CParking[S]): CParking[S] = ???
  }
  // 1.3
  class XParking[-T](vehicles: List[T]) {
    def park(vehicle: T): XParking[T] = new XParking(vehicle :: vehicles)
    def impound(vehicles: List[T]): XParking[T] = new XParking(this.vehicles.filter(v => !vehicles.contains(v)))
    def checkVehicles[V <: T](condition: Boolean): List[V] = ???
    def flatMap[R <: T,S](f: R => XParking[S]): XParking[S] = ???
  }

  /*
      Rule of thumb
      - use covariance = COLLECTION OF THINGS
      - use contravariance = GROUP OF ACTIONS
   */
  // in this example we're using XParking (actions over vehicle)


  // 2. used someone else's API: IList[T] (invariant)
  class CParking2[+T](vehicles: IList[T]) {
    def park[V >: T](vehicle: V): CParking2[T] = ???
    def impound[V >: T](vehicles: IList[V]): CParking[V] = ???
    def checkVehicles[V >: T](condition: Boolean): IList[V] = ???
  }
  // 1.3
  class XParking2[-T](vehicles: IList[T]) {
    def park(vehicle: T): XParking[T] = ???
    def impound[S <: T](vehicles: IList[S]): XParking[S] = ???
    def checkVehicles[S <: T](condition: Boolean): IList[S] = ???
  }

  // 3 flatMap
}
