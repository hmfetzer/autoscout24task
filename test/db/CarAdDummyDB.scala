package db;
import model.CarAd
import scala.util.Try

class CarAdDummyDB extends CarAdDAO {

  // for the DummyDB, a mutable field should be acceptable:
  var carAds: List[CarAd] = List();

  def getAll(): Try[List[CarAd]] = Try {
    throw new NotImplementedError()
  }

  def getOne(id: Int): Try[CarAd] = Try {
    throw new NotImplementedError()
  }

  def save(carAd: CarAd): Try[CarAd] = Try {
    throw new NotImplementedError()
  }

  def update(carAd: CarAd): Try[CarAd] = Try {
    throw new NotImplementedError()
  }

  def delete(id: Int): Try[CarAd] = Try {
    throw new NotImplementedError()
  }

}
