package db;
import model.CarAd
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import java.util.NoSuchElementException
import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

// a Test DB returning only Failure
class CarAdErrorDB extends CarAdDAO {

  val err = new Error("CarAdErrorDB returns only  Errors")

  def init() = {}

  def getAll(ordering: Option[Ordering]): Future[List[CarAd]] = Future {
    throw err
  }

  def getOne(id: Int): Future[CarAd] = Future { throw err }

  def save(carAd: CarAd): Future[CarAd] = Future { throw err }

  def update(carAd: CarAd): Future[CarAd] = Future { throw err }

  def delete(id: Int): Future[CarAd] = Future { throw err }

  def getKnownFuels(): Future[List[String]] = Future { throw err }

}
