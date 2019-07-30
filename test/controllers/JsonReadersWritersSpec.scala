package controllers
import play.api.libs.json.JsString
import org.scalatestplus.play.PlaySpec
import db.TestData
import model._
import play.api.libs.json.Json
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsError
import org.joda.time.LocalDate

class JsonReadesWritersSpec extends PlaySpec with TestData {

  import JsonWritersAndReaders._

  " JSON Readers/Writers " should {

    "read the only Fuels diesel and gasoline " in {
      Json.fromJson[Fuel](JsString("diesel")) mustBe JsSuccess(Diesel)
      Json.fromJson[Fuel](JsString("gasoline")) mustBe JsSuccess(Gasoline)
      Json.fromJson[Fuel](JsString("xxx")) mustBe a[JsError]
    }

    "write the Fuels Diesel and Gasoline " in {
      Json.toJson[Fuel](Diesel) mustBe JsString("diesel")
      Json.toJson[Fuel](Gasoline) mustBe JsString("gasoline")
    }

    "read the LocalDate 2019-07-22" in {
      Json.fromJson[LocalDate](JsString("2019-07-22")) mustBe
        JsSuccess(new LocalDate(2019, 7, 22))
    }

    "write the LocalDate 2019-07-22" in {
      Json.toJson[LocalDate](new LocalDate(2019, 7, 22)) mustBe
        JsString("2019-07-22")
    }

    "automatically read a NewCarAd" in {
      Json.fromJson[CarAd](Json.parse(newAdJson)) mustBe JsSuccess(newAd)
    }

    "automatically read an UsedCarAd" in {
      Json.fromJson[CarAd](Json.parse(usedAdJson)) mustBe JsSuccess(usedAd)
    }

    "should not read an ad with unknown fuel" in {
      Json.fromJson[CarAd](Json.parse(unknownFuelAd)) mustBe a[JsError]
    }

    "should not read an UsedCarAd without mileage or first registration" in {
      Json.fromJson[CarAd](Json.parse(usedAdJsonWithoutMileage)) mustBe a[
        JsError
      ]
      Json.fromJson[CarAd](Json.parse(usedAdJsonWithoutFirstReg)) mustBe a[
        JsError
      ]
    }
  }

}
