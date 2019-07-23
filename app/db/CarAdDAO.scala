package db;
import model.CarAd
import scala.util.Try

trait CarAdDAO {

  // Failure if DB-Exception
  def getAll(): Try[List[CarAd]]

  // Failure if not in DB or DB-Exception
  def getOne(id: Int): Try[CarAd]

  // Failure if already in DB or DB-Exception
  def save(carAd: CarAd): Try[CarAd]

  // Failure if not in DB or DB-Exception
  def update(carAd: CarAd): Try[CarAd]

  // Failure if not in DB or DB-Exception
  def delete(id: Int): Try[CarAd]

}
