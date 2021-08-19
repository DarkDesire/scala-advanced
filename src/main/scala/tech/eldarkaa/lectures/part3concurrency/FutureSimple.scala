package tech.eldarkaa.lectures.part3concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object FutureSimple extends App {

  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  val aFuture = Future {
    calculateMeaningOfLife // calculates on ANOTHER thread
  } // (global) which is passed by the compiler

  println("waiting on the future")
  aFuture.onComplete {
    case Success(value) => println(s"Meaning of life: $value")
    case Failure(e) => println(s"I have failed with $e")
  } // some thread

  Thread.sleep(3000)
}
