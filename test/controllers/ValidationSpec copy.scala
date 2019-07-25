package controllers;

import org.scalatestplus.play.PlaySpec
import db.TestData
import play.api.test.Helpers._
import org.joda.time.LocalDate
import javax.inject._
import db._

class ValidationSpec extends PlaySpec with TestData {

  val cac = new CarAdController(null, new CarAdDummyDAO)
  val knownFuels = List("gasoline", "diesel")

  "CarAdController validation " should {

    "find error with used car and missing mileage/registration" in {
      val ca =
        ad1.copy(newCar = false, mileage = None, firstRegistration = None)
      cac.validateCarAd(ca, knownFuels) must not be None
    }

    "find error with old car and given mileage/registration" in {
      val ca = ad1.copy(
        newCar = true,
        mileage = Some(100),
        firstRegistration = Some(new LocalDate("2015-10-10"))
      )
      cac.validateCarAd(ca, knownFuels) must not be None
    }

    "accept used car with given mileage/registration" in {
      val ca =
        ad1.copy(
          newCar = false,
          mileage = Some(100),
          firstRegistration = Some(new LocalDate("2015-10-10"))
        )
      cac.validateCarAd(ca, knownFuels) mustBe None
    }

    "accept new car with missing mileage/registration" in {
      val ca = ad1.copy(newCar = true, mileage = None, firstRegistration = None)
      cac.validateCarAd(ca, knownFuels) mustBe None
    }

    "not accect fuel h2" in {
      val ca = ad1.copy(fuel = "h2")
      cac.validateCarAd(ca, knownFuels) must not be None
    }

  }

}
