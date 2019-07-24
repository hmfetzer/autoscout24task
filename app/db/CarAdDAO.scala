package db;
import model.CarAd
import scala.util.Try
import com.google.inject.ImplementedBy

@ImplementedBy(classOf[CarAdH2DAO])
trait CarAdDAO {

  case class Ordering(field: String, ascending: Boolean)

  def orderString(od: Option[Ordering]): String =
    od.fold("")(
      o => s"  order by ${o.field} " + (if (o.ascending) "" else " desc")
    )

  def init(): Unit

  // Failure if DB-Exception
  def getAll(ordering: Option[Ordering]): Try[List[CarAd]]

  // Failure if not in DB or DB-Exception
  def getOne(id: Int): Try[CarAd]

  // Failure if already in DB or DB-Exception
  def save(carAd: CarAd): Try[CarAd]

  // Failure if not in DB or DB-Exception
  def update(carAd: CarAd): Try[CarAd]

  // Failure if not in DB or DB-Exception
  def delete(id: Int): Try[CarAd]

  def getKnownFuels(): Try[List[String]]

}
