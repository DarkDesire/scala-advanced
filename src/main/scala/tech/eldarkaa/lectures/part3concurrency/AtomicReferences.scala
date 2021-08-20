package tech.eldarkaa.lectures.part3concurrency

import java.util.concurrent.atomic.AtomicReference

object AtomicReferences extends App {

  // 2 - atomic ops and references

  val atomic = new AtomicReference[Int](2)
  val currentValue = atomic.get() // thread-safe read
  atomic.set(4) // thread-safe write
  atomic.getAndSet(5) // thread-safe combo
  atomic.compareAndSet(38, 56) // thread-safe
  // reference equality

  atomic.updateAndGet(_+1) // thread-safe function run
  atomic.getAndUpdate(_+1)

  atomic.accumulateAndGet(12, _ + _) // thread-safe accumulation
  atomic.getAndAccumulate(12, _ + _)

}
