package controllers
import model.CarAd
import org.joda.time.LocalDate
import scala.util.Try
import play.api.libs.json._

// contains div. implict do serialize/deserialize CarAds
trait JsonWritersAndReaders {
  implicit val localDateWrites = new Writes[LocalDate] {
    def writes(ld: LocalDate) = JsString(ld.toString())
  }
  implicit val loacalDateReads: Reads[LocalDate] = new Reads[LocalDate] {
    def reads(json: JsValue) = {
      Try { LocalDate.parse(json.as[String]) }.fold(
        e => JsError(e.toString),
        d => JsSuccess(d)
      )
    }
  }
  implicit val residentReads = Json.reads[CarAd]
  implicit val residentWrites = play.api.libs.json.Json.writes[CarAd]
}
