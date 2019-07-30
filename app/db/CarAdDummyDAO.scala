package db;

import model.CarAd

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try
import scala.concurrent.Future
import scala.util.Success

// a test-db storing data in memory in a scala list
class CarAdDummyDAO extends CarAdDAO {

  // for the DummyDB, a mutable field should be acceptable:
  var carAds: List[CarAd] = List();

  def init(): Unit = {} // nothing to initialize

  def getAll(ordering: Option[Ordering]): Future[List[CarAd]] = Future {
    carAds
  }

  def getOne(id: Int): Future[CarAd] = Future {
    carAds
      .find(_.id == id)
      .getOrElse(
        throw new NoSuchElementException(s"CarAd with id $id was not found")
      )
  }

  def save(carAd: CarAd): Future[CarAd] =
    getOne(carAd.id) transformWith {
      case Success(ca) =>
        Future {
          throw new IllegalArgumentException(
            s"CarAd with id ${carAd.id} is alread in DB"
          )
        }
      case (_) =>
        carAds = carAd :: carAds
        getOne(carAd.id)
    }

  def update(carAd: CarAd): Future[CarAd] =
    delete(carAd.id).flatMap(_ => save(carAd))

  def delete(id: Int): Future[CarAd] =
    getOne(id).map(carAd => {
      carAds = carAds.filter(_.id != id)
      carAd
    })

  def getKnownFuels(): Future[List[String]] = Future {
    (List("gasoline", "diesel"))
  }

}
