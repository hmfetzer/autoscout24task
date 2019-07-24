package db;

import model.CarAd
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import java.util.NoSuchElementException

// a test-db storing data in memory
class CarAdDummyDB extends CarAdDAO {

  // for the DummyDB, a mutable field should be acceptable:
  var carAds: List[CarAd] = List();

  def init(): Unit = {} // nothing to initialize

  def getAll(ordering: Option[Ordering]): Try[List[CarAd]] = Try {
    carAds
  }

  def getOne(id: Int): Try[CarAd] = Try {
    carAds
      .find(_.id == id)
      .getOrElse(
        throw new NoSuchElementException(s"CarAd with id $id was not found")
      )
  }

  def save(carAd: CarAd): Try[CarAd] = getOne(carAd.id) match {
    case Success(ca) =>
      Failure(
        new IllegalArgumentException(
          s"CarAd with id ${carAd.id} is alread in DB"
        )
      )
    case Failure(_) =>
      carAds = carAd :: carAds
      getOne(carAd.id)

  }

  def update(carAd: CarAd): Try[CarAd] =
    delete(carAd.id).flatMap(_ => save(carAd))

  def delete(id: Int): Try[CarAd] =
    getOne(id).map(carAd => {
      carAds = carAds.filter(_.id != id)
      carAd
    })

  def getKnownFuels(): Try[List[String]] = Success(List("gasoline", "diesel"))

}
