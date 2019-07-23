package db;
import model.CarAd
import scala.util.Try

trait CarAdDAO {

  def getAll(): Try[List[CarAd]]

  def getOne(id: Int): Try[CarAd]

  def save(carAd: CarAd): Try[CarAd]

  def update(carAd: CarAd): Try[CarAd]

  def delete(id: Int): Try[CarAd]

}
