package db;
import model.CarAd
import scala.util.Try
import com.google.inject.ImplementedBy

case class Ordering(field: String, descending: Boolean)

@ImplementedBy(classOf[CarAdH2DAO])
trait CarAdDAO {

  val orderString = "sortby"

  def orderString(od: Option[Ordering]): String =
    od.fold("")(
      o => s"  order by ${o.field} " + (if (o.descending) " desc" else "")
    )

  def init(): Unit

  // Failure, if DB-Exception
  def getAll(ordering: Option[Ordering]): Try[List[CarAd]]

  // Failure, if not in DB or DB-Exception
  def getOne(id: Int): Try[CarAd]

  // Failure, if already in DB or DB-Exception
  def save(carAd: CarAd): Try[CarAd]

  // Failure, if not in DB or DB-Exception
  def update(carAd: CarAd): Try[CarAd]

  // Failure, if not in DB or DB-Exception
  def delete(id: Int): Try[CarAd]

  def getKnownFuels(): Try[List[String]]

}
