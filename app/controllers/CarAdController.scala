package controllers

import db.CarAdDummyDB
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import scala.util.parsing.json.JSONObject
import model.CarAd
import org.joda.time.LocalDate

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class CarAdController @Inject()(
    @Inject() cc: ControllerComponents
) extends AbstractController(cc)
    with JsonWritersAndReaders {

  val db = new CarAdDummyDB
  db.init()
  val kf = db.getKnownFuels().fold(throw _, (ls: List[String]) => ls);

  def getAll() = Action { implicit request: Request[AnyContent] =>
    constructResponse(db.getAll())
  }

  def getOne(id: Int) = Action { implicit request: Request[AnyContent] =>
    constructResponse(db.getOne(id))
  }

  def add(id: Int) = Action(parse.tolerantJson) {
    implicit request: Request[JsValue] =>
      request.body
        .validate[CarAd]
        .fold(
          e => BadRequest(e.toString),
          ca =>
            validateCarAd(ca, kf)
              .fold(constructResponse(db.save(ca)))(BadRequest(_))
        )
  }

  def update(id: Int) = Action(parse.tolerantJson) {
    implicit request: Request[JsValue] =>
      request.body
        .validate[CarAd]
        .fold(
          e => BadRequest(e.toString),
          ca =>
            validateCarAd(ca, kf)
              .fold(constructResponse(db.update(ca)))(BadRequest(_))
        )
  }

  def delete(id: Int) = Action { implicit request: Request[AnyContent] =>
    constructResponse(db.getAll())
  }

  // validation acording to the task description - optionally returns an error message
  def validateCarAd(ca: CarAd, knownFuels: List[String]): Option[String] = {
    if (ca.newCar && (ca.mileage.isDefined || ca.firstRegistration.isDefined)) {
      Some("Mileage and first registration should not be given for new cars!")
    } else if (!ca.newCar && (ca.mileage.isEmpty || ca.firstRegistration.isEmpty)) {
      Some("Mileage and first registration should be given for used cars!")
    } else if (!knownFuels.contains(ca.fuel)) {
      Some(s"Fuel should be one of: " + knownFuels.mkString(", "))
    } else {
      None
    }
  }

  def constructResponse[T: Writes](dbRes: Try[T]) = {
    dbRes match {
      case Success(t)                           => Ok(Json.toJson[T](t))
      case Failure(e: NoSuchElementException)   => NotFound(e.toString)
      case Failure(e: IllegalArgumentException) => BadRequest(e.toString)
      case Failure(e)                           => InternalServerError(e.toString)
    }
  }

}
