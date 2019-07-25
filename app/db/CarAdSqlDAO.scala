package db;

import model.CarAd
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import java.util.NoSuchElementException
import com.google.common.collect.ImmutableMap
import javax.inject.Inject
import java.sql.Statement
import java.sql.Connection
import java.sql.ResultSet
import scala.collection.mutable
import org.joda.time.LocalDate
import org.slf4j.LoggerFactory
import play.api.db.Database

// contains code valid for (hopefully) all SQL databases
trait CarAdSqlDAO extends CarAdDAO {

  val log = LoggerFactory.getLogger(this.getClass)
  log.debug(this.getClass.toString + " loaded")

  val db: Database

  def dateToSql(d: LocalDate): String

  def getAll(ordering: Option[Ordering]): Try[List[CarAd]] = Try {
    db.withConnection { con: Connection =>
      val sql = "select * from carads " + orderString(ordering)
      log.debug(sql)
      val rs = con.createStatement.executeQuery(sql)
      val res = mutable.Buffer[CarAd]()
      while (rs.next) res.append(getOneCarAd(rs))
      res.toList
    }
  }

  private def getOneCarAd(rs: ResultSet): CarAd = {
    val id = rs.getInt("id")
    val title = rs.getString("title")
    val fuel = rs.getString("fuel")
    val price = rs.getInt("price")
    val newCar = rs.getBoolean("newCar")
    val ml = rs.getInt("mileage")
    val mileage = if (rs.wasNull()) None else Some(ml)
    val reg = rs.getDate("firstRegistration")
    val firstReg: Option[LocalDate] =
      if (rs.wasNull()) None else Some(new LocalDate(reg))
    CarAd(id, title, fuel, price, newCar, mileage, firstReg)
  }

  def getOne(id: Int): Try[CarAd] = Try {
    db.withConnection { con: Connection =>
      val sql = "select * from carads where id = " + id
      log.debug(sql)
      val rs = con.createStatement.executeQuery(sql)
      if (rs.next) getOneCarAd(rs)
      else throw new NoSuchElementException(s"CarAd with id $id was not found")
    }
  }

  def save(carAd: CarAd): Try[CarAd] = Try {
    db.withConnection { con: Connection =>
      import carAd._
      val sql = s""" 
        insert into carads values (
          $id,
          '$title',
          '$fuel',
          $price,
          $newCar,
          ${mileage.fold("null")(_.toString)},
          ${firstRegistration.fold("null")(dateToSql(_))}
        ) 
        """
      log.debug(sql)
      con.createStatement.execute(sql)
      carAd
    }
  }

  // Not efficient - but very easy to implement:
  def update(carAd: CarAd): Try[CarAd] =
    delete(carAd.id).flatMap(_ => save(carAd))

  def delete(id: Int): Try[CarAd] =
    getOne(id).flatMap(
      ca =>
        Try {
          db.withConnection { con =>
            val sql = "delete from carads where id = " + id
            log.debug(sql)
            val stmt = con.createStatement
            stmt.execute(sql)
            if (stmt.getUpdateCount() == 1) ca
            else
              throw new Error(
                "Not one record was deleted but " + stmt.getUpdateCount()
              )
          }
        }
    )

  // the known fuels are stored in a db table.
  // Fuels can be added or removed without recompile!
  def getKnownFuels(): Try[List[String]] = Try {
    db.withConnection { con: Connection =>
      val sql = "select * from fuels"
      val rs = con.createStatement.executeQuery(sql)
      var res = List[String]()
      while (rs.next) res = rs.getString("fuel") :: res
      res
    }
  }

}