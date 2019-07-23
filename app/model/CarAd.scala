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
case class CarAd(
    id: Int,
    title: String,
    fuel: String,
    price: Int,
    // The fieldname new can not be used because it is a reserved word.
    // Changing it to newCar should not be a problem. (Using as scala Symbol
    // `new` could be another option.)
    newCar: Boolean,
    mileage: Option[Int],
    // Using joda for date without time, instead of the old java Date class
    firstRegistration: Option[LocalDate]
)
