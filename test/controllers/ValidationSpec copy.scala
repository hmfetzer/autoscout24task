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

    "not accect fuel h2" in {
      val ca = usedAd.copy(fuel = "h2")
      cac.validateCarAd(ca, knownFuels) must not be None
    }

  }

}
