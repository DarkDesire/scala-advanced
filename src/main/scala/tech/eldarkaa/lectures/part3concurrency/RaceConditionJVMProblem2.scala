package tech.eldarkaa.lectures.part3concurrency

object RaceConditionJVMProblem2 extends App {

  class BankAccount(@volatile var amount: Int) {
    override def toString = "" + amount
  }

  def buy(account: BankAccount, thing: String, price: Int) = {
    account.amount -= price
    /*println("I've bought " + thing)
    println("My account is now "+ account)
    */
  }

  for (_ <- 1 to 10000){
    val account = new BankAccount(50000)
    val thread1 = new Thread(() => buySafe(account, "shoes", 3000))
    val thread2 = new Thread(() => buySafe(account, "iPhone12", 4000))
    thread1.start()
    thread2.start()
    Thread.sleep(100)
  }
  /*
    thread1 (shoes): 50000
      - account = 50000 - 3000 = 47000
    thread2 (iPhone): 50000
      - account = 50000 - 4000 = 46000
      overwrites the of the account.amount
   */


  // option #1 : use synchronized
  def buySafe(account: BankAccount, thing: String, price: Int) = {
    account.synchronized {
      // no two threads can evaluate this at the same time
      account.amount -= price
      println("I've bought " + thing)
      println("My account is now " + account)
    }
  }

  // option #2: use @volatile

}
