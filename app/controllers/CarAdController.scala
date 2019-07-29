package controllers

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
import org.slf4j.LoggerFactory
import _root_.db.CarAdDAO
import _root_.db.Ordering

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class CarAdController @Inject()(
    cc: ControllerComponents,
    db: CarAdDAO
) extends AbstractController(cc)
    with JsonWritersAndReaders {

  val log = LoggerFactory.getLogger(this.getClass);
  db.init()
  val kf = db.getKnownFuels().fold(throw _, (ls: List[String]) => ls);
  log.debug(s"Known Fuels: " + kf.mkString(", "))

  def getAll() = Action { implicit request: Request[AnyContent] =>
    val order = orderParam(request.getQueryString(db.orderString))
    log.debug(order.toString)
    constructResponse(db.getAll(order))
  }

  private def orderParam(os: Option[String]): Option[Ordering] = os.flatMap(
    s =>
      if (s.length < 1) None
      else {
        val s0 = s(0)
        val field = if (List('-', '+', ' ') contains s0) s.drop(1) else s
        Some(Ordering(field, s0 == '-'))
      }
  )

  def getOne(id: Int) = Action { implicit request: Request[AnyContent] =>
    constructResponse(db.getOne(id))
  }

  // Expects a NEW id provided in the JSON-Body
  def add() = Action(parse.tolerantJson) { implicit request: Request[JsValue] =>
    validateAndProcessJsonCarAd(db.save)
  }

  // Expects a USED id provided in the JSON-Body
  def update = Action(parse.tolerantJson) {
    implicit request: Request[JsValue] =>
      validateAndProcessJsonCarAd(db.update)
  }

  def delete(id: Int) = Action { implicit request: Request[AnyContent] =>
    constructResponse(db.delete(id))
  }

  def validateAndProcessJsonCarAd(
      f: CarAd => Try[CarAd]
  )(implicit request: Request[JsValue]): Result = {
    request.body
      .validate[CarAd]
      .fold(
        e => BadRequest(e.toString),
        ca =>
          validateCarAd(ca, kf)
            .fold(constructResponse(f(ca)))(BadRequest(_))
      )
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

  def constructResponse[T: Writes](dbRes: Try[T]): Result = {
    dbRes match {
      case Success(t)                           => Ok(Json.toJson[T](t))
      case Failure(e: NoSuchElementException)   => NotFound(e.toString)
      case Failure(e: IllegalArgumentException) => BadRequest(e.toString)
      case Failure(e)                           => InternalServerError(e.toString)
    }
  }

}
