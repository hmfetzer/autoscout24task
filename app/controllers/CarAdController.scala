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
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class CarAdController @Inject()(
    cc: ControllerComponents,
    db: CarAdDAO
) extends AbstractController(cc) {

  import JsonWritersAndReaders._

  val log = LoggerFactory.getLogger(this.getClass);
  db.init()

  def getAll() = Action.async { implicit request: Request[AnyContent] =>
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

  def getOne(id: Int) = Action.async { implicit request: Request[AnyContent] =>
    constructResponse(db.getOne(id))
  }

  // Expects a NEW id provided in the JSON-Body
  def add() = Action(parse.tolerantJson).async {
    implicit request: Request[JsValue] =>
      validateAndProcessJsonCarAd(db.save)
  }

  // Expects a USED id provided in the JSON-Body
  def update = Action(parse.tolerantJson).async {
    implicit request: Request[JsValue] =>
      validateAndProcessJsonCarAd(db.update)
  }

  def delete(id: Int) = Action.async { implicit request: Request[AnyContent] =>
    constructResponse(db.delete(id))
  }

  def validateAndProcessJsonCarAd(
      f: CarAd => Future[CarAd]
  )(implicit request: Request[JsValue]): Future[Result] = {
    request.body
      .validate[CarAd]
      .fold(
        e => Future { BadRequest(e.toString) },
        ca => constructResponse(f(ca))
      )
  }

  def constructResponse[T: Writes](dbRes: Future[T]): Future[Result] =
    dbRes map { t =>
      Ok(Json.toJson[T](t))
    } recover {
      case e: NoSuchElementException   => NotFound(e.toString)
      case e: IllegalArgumentException => BadRequest(e.toString)
      case e                           => InternalServerError(e.toString)
    }

}
