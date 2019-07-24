package db;
import model.CarAd
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import java.util.NoSuchElementException

// a Test DB returning only Failure
class CarAdErrorDB extends CarAdDAO {

  val err = new Error("CarAdErrorDB returns only  Errors")

  def init() = {}

  def getAll(ordering: Option[Ordering]): Try[List[CarAd]] = Failure(err)

  def getOne(id: Int): Try[CarAd] = Failure(err)

  def save(carAd: CarAd): Try[CarAd] = Failure(err)

  def update(carAd: CarAd): Try[CarAd] = Failure(err)

  def delete(id: Int): Try[CarAd] = Failure(err)

  def getKnownFuels(): Try[List[String]] = Failure(err)

}
