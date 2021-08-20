package tech.eldarkaa.lectures.part3concurrency

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicReference
import scala.collection.parallel.CollectionConverters._
import scala.collection.parallel.{ForkJoinTaskSupport, Task, TaskSupport}
import scala.collection.parallel.immutable.ParVector
object ParallelUtils extends App {

  // 1 - parallel collections

  val parList = List(1,2,3).par

  val aParVector = ParVector[Int](1,2,3)
  /*
  Seq
  Vector
  Array
  Map - Hash, TrieMap
  Set - Hash, TrieMap
   */

  def measure[T](operation: => T): Long = {
    val startTime = System.currentTimeMillis()
    operation
    System.currentTimeMillis() - startTime
  }

  val list = (1 to 1000000).toList
  val serialTime = measure(list.map(_+1))
  println("serial time is: "+serialTime)
  val parallelTime = measure(list.par.map(_+1))
  println("parallel time is: "+parallelTime)

  /*
    Map-reduce model
    - split the elements into chunks - Splitter
    - operation
    - recombine - Combiner
   */

  // map, flatMap, filter - safe

  // reduce, fold - not safe with non-associative operators
  println(List(1,2,3).reduce(_ - _))
  println(List(1,2,3).par.reduce(_ - _))

  // synchronization on value of result
  var sum = 0
  List(1,2,3,4,5).par.foreach(sum += _)
  println(sum) // race condition

  // configuring
  aParVector.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool((2)))
  /*
    alternative
      - ThreadPoolTaskSupport - deprecated
      - ExecutionContextTaskSupport(EC)
      - or custom
   */
   aParVector.tasksupport = new TaskSupport{
     override def execute[R, Tp](fjtask: Task[R, Tp]): () => R = ???
     override def executeAndWaitResult[R, Tp](task: Task[R, Tp]): R = ???
     override def parallelismLevel: Int = ??? // cpu cores
     override val environment: AnyRef = ???
   }
}
