package model;
import org.joda.time.LocalDate

/*
    fields for CarAd:

    id (required): int or guid, choose whatever is more convenient for you;
    title (required): string, e.g. "Audi A4 Avant";
    fuel (required): gasoline or diesel, use some type which could be extended in the future by adding additional fuel types;
    price (required): integer;
    new (required): boolean, indicates if car is new or used;
    mileage (only for used cars): integer;
    first registration (only for used cars): date without time.

 */

/*
    Datatype for fuel changed:
    It seems easier to store Fuel as as String and perform
    an explicit validation based on a list of allowed values,
    possibly stored in the db!
 */

/* Using a case class makes transfering from and to JSON easier */
sealed trait CarAd {
  val id: Int
  val title: String
  val fuel: String
  val price: Int
}

case class NewCarAd(
    id: Int,
    title: String,
    fuel: String,
    price: Int
) extends CarAd

case class UsedCarAd(
    id: Int,
    title: String,
    fuel: String,
    price: Int,
    mileage: Int,
    firstRegistration: LocalDate
) extends CarAd
