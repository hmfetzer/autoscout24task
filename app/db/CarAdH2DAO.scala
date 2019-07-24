package db;

import model.CarAd
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import java.util.NoSuchElementException
import com.google.common.collect.ImmutableMap
import play.db.Database
import javax.inject.Inject

class CarAdH2DAO @Inject()(db: Database) extends CarAdDAO {

  def init(): Unit = {
    val conn = db.getConnection()

    try {
      val stmt = conn.createStatement
      val rs = stmt.executeQuery("SELECT 9 as testkey ")

      while (rs.next()) {
        // outString += rs.getString("testkey")
      }
    } finally {
      conn.close()
    }
  }

  def getAll(): Try[List[CarAd]] = ???

  def getOne(id: Int): Try[CarAd] = ???

  def save(carAd: CarAd): Try[CarAd] = ???

  def update(carAd: CarAd): Try[CarAd] = ???

  def delete(id: Int): Try[CarAd] = ???

  def getKnownFuels(): Try[List[String]] = ???
}
