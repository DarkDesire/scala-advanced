package tech.eldarkaa.lectures.part3concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Random, Success}

object FuturePromises extends App {
  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object Database {
    // fetching delay
    val random = new Random()
    def delay(ms: Int): Unit = Thread.sleep(random.nextInt(ms))
  }
  object BankingApp {
    val name = "Rock the JVM banking"

    def fetchUser(name: String): Future[User] = Future {
      Database.delay(500)
      User(name)
    }
    def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future {
      Database.delay(1000)
      Transaction(user.name, merchantName, amount, "Success")
    }
    def purchase(username: String, item: String, merchantName: String, cost: Double): String = {
      // fetch the user from db
      // check money
      // create a transaction
      // WAIT for the transaction to finish
      val transactionStatusFuture = for {
        user <- fetchUser(username)
        transaction <- createTransaction(user, merchantName, cost)
      } yield transaction.status

      Await.result(transactionStatusFuture, 2.seconds)
    }
  }
  println(BankingApp.purchase("Daniel", "iPhone 12", "store", 3000d))

  // promises
  val promise = Promise[Int]() // "controller" over a future
  val future = promise.future

  // thread 1 - "consumer"
  future.foreach{value => println("[consumer] I've received "+ value)}

  // thread 2 - "producer"
  val producer = new Thread(() =>{
    println("[producer] crunching numbers...")
    Thread.sleep(500)
    // "fulfilling" the promise
    promise.success(42)
    println("[producer] done")
  })
  producer.start()
  Thread.sleep(1000)

}
