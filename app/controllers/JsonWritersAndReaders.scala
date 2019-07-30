package controllers
import model._
import org.joda.time.LocalDate
import scala.util.Try
import play.api.libs.json._
import scala.util.Failure
import scala.util.Success

// contains div. implict do serialize/deserialize CarAds
trait JsonWritersAndReaders {
  implicit val localDateWrites = new Writes[LocalDate] {
    def writes(ld: LocalDate) = JsString(ld.toString())
  }
  implicit val loacalDateReads: Reads[LocalDate] = new Reads[LocalDate] {
    def reads(json: JsValue): JsResult[LocalDate] = {
      Try { LocalDate.parse(json.as[String]) }.fold(
        e => JsError(e.toString),
        d => JsSuccess(d)
      )
    }
  }

  implicit val carAdReads: Reads[CarAd] = new Reads[CarAd] {
    def reads(jsv: JsValue): JsResult[CarAd] = {
      val id = (jsv \ "id").validate[Int]
      val title = (jsv \ "title").validate[String]
      val fuel = (jsv \ "fuel").validate[String]
      val price = (jsv \ "price").validate[Int]
      val used = (jsv \ "mileage").isDefined || (jsv \ "firstRegistrations").isDefined
      val mileage = (jsv \ "mileage").validate[Int]
      val firstRegistration = (jsv \ "firstRegistration").validate[LocalDate]
      for {
        i <- id
        t <- title
        f <- fuel
        p <- price
        m <- mileage
        r <- firstRegistration
      } yield
        if (used) new UsedCarAd(i, t, f, p, m, r) else new NewCarAd(i, t, f, p)
    }
  }

  implicit val newCarAdWrites: Writes[NewCarAd] = new Writes[NewCarAd] {
    def writes(nca: NewCarAd): JsValue = Json.obj(
      "id" -> nca.id,
      "title" -> nca.title,
      "fuel" -> nca.fuel,
      "price" -> nca.price
    )
  }

  implicit val usedCarAdWrites: Writes[UsedCarAd] = new Writes[UsedCarAd] {
    def writes(uca: UsedCarAd): JsValue = Json.obj(
      "id" -> uca.id,
      "title" -> uca.title,
      "fuel" -> uca.fuel,
      "price" -> uca.price,
      "mileage" -> uca.mileage,
      "firstRegistration" -> uca.firstRegistration
    )
  }

  implicit val carAdWrites: Writes[CarAd] = new Writes[CarAd] {
    def writes(ca: CarAd): JsValue = ca match {
      case nca: NewCarAd  => newCarAdWrites.writes(nca)
      case uca: UsedCarAd => usedCarAdWrites.writes(uca)
    }
  }

}
