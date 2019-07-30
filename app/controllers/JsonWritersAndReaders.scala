package controllers

import model._
import org.joda.time.LocalDate
import play.api.libs.json._
import scala.util.Try
import scala.util.Failure
import scala.util.Success

// contains some implicts to read/write CarAds from/to JSON
object JsonWritersAndReaders {
  implicit val loacalDateReads: Reads[LocalDate] = new Reads[LocalDate] {
    def reads(json: JsValue): JsResult[LocalDate] = {
      Try { LocalDate.parse(json.as[String]) }.fold(
        e => JsError(e.toString),
        d => JsSuccess(d)
      )
    }
  }
  implicit val localDateWrites = new Writes[LocalDate] {
    def writes(ld: LocalDate) = JsString(ld.toString())
  }

  implicit val fuelReads: Reads[Fuel] = new Reads[Fuel] {
    def reads(jsv: JsValue): JsResult[Fuel] = jsv match {
      case JsString(fs) =>
        Try { Fuel(fs) } match {
          case Success(fuel) => JsSuccess(fuel)
          case Failure(e)    => JsError(e.toString)
        }
      case _ => JsError(s"Cannot convert JsValue $jsv to Fuel")
    }
  }
  implicit val fuelWrites: Writes[Fuel] = new Writes[Fuel] {
    def writes(f: Fuel): JsValue = JsString(f.toString)
  }

  implicit val newCarAdReads = Json.reads[NewCarAd]
  implicit val newCarAdWrites = play.api.libs.json.Json.writes[NewCarAd]

  implicit val usedCarAdReads = Json.reads[UsedCarAd]
  implicit val usedCarAdWrites = play.api.libs.json.Json.writes[UsedCarAd]

  implicit val carAdReads: Reads[CarAd] = new Reads[CarAd] {
    def reads(jsv: JsValue): JsResult[CarAd] =
      if ((jsv \ "mileage").isDefined || (jsv \ "firstRegistration").isDefined) {
        usedCarAdReads.reads(jsv)
      } else {
        newCarAdReads.reads(jsv)
      }
  }
  implicit val carAdWrites: Writes[CarAd] = new Writes[CarAd] {
    def writes(ca: CarAd): JsValue = ca match {
      case nca: NewCarAd  => newCarAdWrites.writes(nca)
      case uca: UsedCarAd => usedCarAdWrites.writes(uca)
    }
  }

}
