package db;

import model.CarAd
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import java.util.NoSuchElementException
import com.google.common.collect.ImmutableMap
import play.db.Database
import javax.inject.Inject
import java.sql.Statement
import java.sql.Connection

class CarAdH2DAO @Inject()(db: Database) extends CarAdDAO {

  def init(): Unit = db.withConnection { con: Connection =>
    con.createStatement.execute(createDb)
  }

  def getAll(): Try[List[CarAd]] = ???

  def getOne(id: Int): Try[CarAd] = ???

  def save(carAd: CarAd): Try[CarAd] = ???

  def update(carAd: CarAd): Try[CarAd] = ???

  def delete(id: Int): Try[CarAd] = ???

  def getKnownFuels(): Try[List[String]] = Try {
    db.withConnection { con: Connection =>
      val sql = "select * from fuels"
      val rs = con.createStatement.executeQuery(sql)
      var res = List[String]()
      while (rs.next) res = rs.getString("fuel") :: res
      res
    }
  }

  val createDb = """
      drop table if exists carads;
      create table carads (
        id        int primary key,
        title     varchar(255) not null,
        fuel      varchar(255) not null,
        price     int not null,
        newCar    boolean not null,
        mileage   int,
        firstRegistration date
      );

      drop table if exists fuels;
      create table fuels (
        fuel  varchar(255)
      );
      insert into fuels values ('gasoline');
      insert into fuels values ('diesel');
  """
}
