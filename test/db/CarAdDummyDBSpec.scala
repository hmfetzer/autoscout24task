package db
import org.scalatestplus.play.PlaySpec
import scala.util.Success
import scala.util.Failure
import model.Fuel
import org.joda.time.LocalDate
import model.CarAd

// some quick (and partly trivial) tests for the DummyDB
class CarAdDummyDBSpec extends PlaySpec {

  val ad1 = CarAd(
    id = 1,
    title = "TestAd 1",
    fuel = Fuel.diesel,
    price = 2,
    newCar = false,
    mileage = 3,
    firstRegistration = new LocalDate(2015, 2, 2)
  )

  "CarAdDummyDB " should {

    "initially be empty " in {
      val db = new CarAdDummyDB
      db.getAll() mustBe Success(List())
    }

    "initially not have an carAd with id 1" in {
      val db = new CarAdDummyDB
      db.getOne(1) mustBe Failure(_: NoSuchElementException)
    }

    "initially not enable update for carAd with id 1" in {
      val db = new CarAdDummyDB
      db.update(ad1) mustBe Failure(_: NoSuchElementException)
    }

    "initially not delete a carAd with id 1" in {
      val db = new CarAdDummyDB
      db.delete(1) mustBe Failure(_: NoSuchElementException)
    }

    " enable adding an ad " in {
      val db = new CarAdDummyDB
      db.save(ad1) mustBe Success(ad1)
    }
    " not enable adding an ad twice" in {
      val db = new CarAdDummyDB
      db.save(ad1) mustBe Success(ad1)
      db.save(ad1) mustBe Failure(_: IllegalArgumentException)
    }

    "contain an element after adding" in {
      val db = new CarAdDummyDB
      db.save(ad1) mustBe Success(ad1)
      db.getOne(ad1.id) mustBe Success(ad1)
    }

    " enable deleting an element" in {
      val db = new CarAdDummyDB
      db.save(ad1) mustBe Success(ad1)
      db.delete(ad1.id) mustBe Success(ad1)
      // after deleting db should be empty
      db.getAll() mustBe Success(List())
    }

    " update an existing element " in {
      val db = new CarAdDummyDB
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
