package tech.eldarkaa.lectures.part3concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

object FuturesMiniSocialNetwork extends App {

  // mini social network
  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile): Unit =
      println(s"${this.name} poking ${anotherProfile.name}")
  }

  object Database {
    // fetching delay
    val random = new Random()

    def delay(ms: Int): Unit = Thread.sleep(random.nextInt(ms))
  }

  object SocialNetwork {
    // "database"
    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.0-dummy" -> "Dummy"
    )
    val friends = Map(
      "fb.id.1-zuck" -> "fb.id.2-bill"
    )

    // API
    def fetchProfile(id: String): Future[Profile] = Future {
      // Fetching from DB
      Database.delay(300)
      Profile(id, names(id))
    }
    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      // Fetching from DB
      Database.delay(400)
      val bfsId = friends(profile.id)
      Profile(bfsId, names(bfsId))
    }
  }

  val f = for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } yield mark.poke(bill)

  Thread.sleep(2000)


  /// recover & fallbacks
  val aProfile = SocialNetwork.fetchProfile("unknown id").recover{
    case e: Throwable => Profile("fb.id.0-dummy", "Forever alone")
  }

  val aFetchedProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recoverWith{
    case e: Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")
      // if we now for sure
  } // F1.recoverWith(F2) , F2 starts computing only after F1 fail

  val fallbackResult =  SocialNetwork.fetchProfile("unknown id")
    .fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))
  // F1.fallbackTo(F2) , F2 starts computing in parallel
}
