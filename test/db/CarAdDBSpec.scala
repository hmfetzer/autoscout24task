package db
import org.scalatestplus.play.PlaySpec
import scala.util.Success
import scala.util.Failure
import org.scalatestplus.play.guice.GuiceOneAppPerTest

// some quick (and partly trivial) tests for the DummyDB
trait CarAdDBSpec extends PlaySpec with GuiceOneAppPerTest with TestData {

  def initDB: CarAdDAO

  "CarAdDummyDB " should {

    "initially be empty " in {
      val db = initDB
      db.getAll(None) mustBe Success(List())
    }

    "initially not have an carAd with id 1" in {
      val db = initDB
      db.getOne(1) mustBe Failure(_: NoSuchElementException)
    }

    "initially not enable update for carAd with id 1" in {
      val db = initDB
      db.update(ad1) mustBe Failure(_: NoSuchElementException)
    }

    "initially not delete a carAd with id 1" in {
      val db = initDB
      db.delete(1) mustBe Failure(_: NoSuchElementException)
    }

    " enable adding an ad " in {
      val db = initDB
      db.save(ad1) mustBe Success(ad1)
    }
    " not enable adding an ad twice" in {
      val db = initDB
      db.save(ad1) mustBe Success(ad1)
      db.save(ad1) mustBe Failure(_: Throwable)
    }

    "contain an element after adding" in {
      val db = initDB
      db.save(ad1) mustBe Success(ad1)
      db.getOne(ad1.id) mustBe Success(ad1)
    }

    " enable deleting an element" in {
      val db = initDB
      db.save(ad1) mustBe Success(ad1)
      db.delete(ad1.id) mustBe Success(ad1)
      // after deleting db should be empty
      db.getAll(None) mustBe Success(List())
    }

    " update an existing element " in {
      val db = initDB
      db.save(ad1) mustBe Success(ad1)
      val newTitle = "new Title"
      val ad2 = ad1.copy(title = newTitle)
      // update should return the new element
      db.update(ad2) match {
        case Failure(_) => fail()
        case Success(adNew) =>
          adNew.title mustBe newTitle
      }
      // reading from db should also return new element
      db.getOne(ad2.id) match {
        case Failure(_) => fail()
        case Success(adNew) =>
          adNew.title mustBe newTitle
      }
    }

  }

}
