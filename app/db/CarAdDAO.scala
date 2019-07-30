package db;
import model.CarAd
import com.google.inject.ImplementedBy
import scala.concurrent.Future

case class Ordering(field: String, descending: Boolean)

// interface, to be implemented for different databases
@ImplementedBy(classOf[CarAdH2DAO])
trait CarAdDAO {

  val orderString = "sortby"

  def orderString(od: Option[Ordering]): String =
    od.fold("")(
      o => s"  order by ${o.field} " + (if (o.descending) " desc" else "")
    )

  def init(): Unit

  // Failure, if DB-Exception
  def getAll(ordering: Option[Ordering]): Future[List[CarAd]]

  // Failure, if not in DB or DB-Exception
  def getOne(id: Int): Future[CarAd]

  // Failure, if already in DB or DB-Exception
  def save(carAd: CarAd): Future[CarAd]

  // Failure, if not in DB or DB-Exception
  def update(carAd: CarAd): Future[CarAd]

  // Failure, if not in DB or DB-Exception
  def delete(id: Int): Future[CarAd]

  def getKnownFuels(): Future[List[String]]

}
