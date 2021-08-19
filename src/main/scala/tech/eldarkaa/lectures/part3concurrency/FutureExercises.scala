package tech.eldarkaa.lectures.part3concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Random, Success, Try}

object FutureExercises extends App {
 /*
 Exercise.
 1) fulfill a future IMMEDIATELY with a value
 2) inSequence(fa , fb)
 3) first(fa,fb) => new future with the first value
 4) last(fa,fb) => new future with the last value
 5) retryUntil( action: () => Future[T], condition: T => Boolean): Future[T]
  */

  // 1
  val future = Future.successful(42)
  // future.foreach(v => println("Here is your value: "+v))

  // 2 inSequence(fa,fb)
  def inSequence[A,B](fa: Future[A], fb: Future[B]): Future[B]=  fa.flatMap(_ =>fb)


  def fast = Future{Thread.sleep(100);"fast"}
  def long = Future{Thread.sleep(1000);"long"}
  inSequence(fast, long).foreach(v => println("inSequence value: "+v))

  // 3 first
  def first[T](fa: Future[T], fb: Future[T]): Future[T] = {
    val promise = Promise[T]
    fa.onComplete(promise.tryComplete)
    fb.onComplete(promise.tryComplete)
    promise.future
  }
  first(fast,long).foreach(v => println("first value: "+v))

  // 4 last
  def last[T](fa: Future[T], fb: Future[T]): Future[T] = {
    val bothPromise = Promise[T] // which both future will try to complete
    val lastPromise = Promise[T] // will be completed by the last future

    def checkAndComplete = (result: Try[T]) => {
      if (!bothPromise.tryComplete(result))
        lastPromise.complete(result)
    }
    fa.onComplete(checkAndComplete)
    fb.onComplete(checkAndComplete)
    lastPromise.future
  }
  last(fast,long).foreach(v => println("last value: "+v))

  // 5
  // retryUntil
  def retryUntil[T]( action: () => Future[T], condition: T => Boolean): Future[T] = {
    action().filter(condition).recoverWith(_ => retryUntil(action, condition))
  }

  val random = new Random
  val action = () => Future {
    Thread.sleep(100)
    val nextValue = random.nextInt(100)
    println("Generated next value: "+nextValue)
    nextValue
  }
  retryUntil(action, (x: Int) => x < 10)
    .foreach(v => println("retryUntil settled at: "+v))

  Thread.sleep(1000)

}
