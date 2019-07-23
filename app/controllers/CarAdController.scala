package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import db.CarAdDAO
import db.CarAdDummyDB
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import scala.util.parsing.json.JSONObject
import model.CarAd
import org.joda.time.LocalDate
import akka.http.javadsl.model.headers.Location

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class CarAdController @Inject()(@Inject() cc: ControllerComponents)
    extends AbstractController(cc) {

  // implicits for automatic conversion from and to JSON
  implicit val localDateWrites = new Writes[LocalDate] {
    def writes(ld: LocalDate) = JsString(ld.toString())
  }
  implicit val loacalDateReads: Reads[LocalDate] = new Reads[LocalDate] {
    def reads(json: JsValue) = {
      println(json.toString())
      Try { LocalDate.parse(json.as[String]) }.fold(
        e => JsError(e.toString),
        d => JsSuccess(d)
      )
    }
  }
  implicit val residentReads = Json.reads[CarAd]
  implicit val residentWrites = Json.writes[CarAd]
  val db = new CarAdDummyDB();

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
          ca => constructResponse(db.save(ca))
        )
  }

  def update(id: Int) = Action(parse.tolerantJson) {
    implicit request: Request[JsValue] =>
      request.body
        .validate[CarAd]
        .fold(
          e => BadRequest(e.toString),
          ca => constructResponse(db.update(ca))
        )
  }

  def delete(id: Int) = Action { implicit request: Request[AnyContent] =>
    constructResponse(db.getAll())
  }

  def constructResponse[T: Writes](dbRes: Try[T]) = {
    dbRes match {
      case Success(t)                           => Ok(Json.toJson[T](t))
      case Failure(e: NoSuchElementException)   => NotFound(e.toString)
      case Failure(e: IllegalArgumentException) => BadRequest(e.toString)
      case Failure(e)                           => InternalServerError(e.toString)
    }
  }

  // def carAdToJson(ca: CarAd): JsValue = Json.toJson(ca)

  def JsonToCarAd(json: JsValue): CarAd = ???

  def getCarFromBody(implicit request: Request[AnyContent]): Try[CarAd] = ???

}
