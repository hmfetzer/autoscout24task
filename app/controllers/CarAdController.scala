package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class CarAdController @Inject()(cc: ControllerComponents)
    extends AbstractController(cc) {

  def getAll() = Action { implicit request: Request[AnyContent] =>
    Ok("getAll()")
  }

  def getOne(id: Int) = Action { implicit request: Request[AnyContent] =>
    Ok(s"getOne($id)")
  }

  def add(id: Int) = Action { implicit request: Request[AnyContent] =>
    Ok(s"add($id)")
  }

  def update(id: Int) = Action { implicit request: Request[AnyContent] =>
    Ok(s"update($id)")
  }

  def delete(id: Int) = Action { implicit request: Request[AnyContent] =>
    Ok(s"delete($id)")
  }
}
