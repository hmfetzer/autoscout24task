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
    In a first attempt, I use an (kind of) enum for the Fuel type.
    This is not adequate for a real System: It should be
    possible to add new fuels without recompiling! The possible fuel
    values could, for instance, be stored in a lookup-table in the database.
 */
sealed trait Fuel;
object gasoline extends Fuel
object diesel extends Fuel

/* Using a case class makes transfering from and to JSON easier */
case class CarAd(
    id: Int,
    title: String,
    fuel: Fuel,
    price: Int,
    // The fieldname new can not be used because it is a reserved word.
    // Changing it to newCar should not be a problem. (Using as scala Symbol
    // `new` could be another option.)
    newCar: Boolean,
    mileage: Int,
    // Using joda for date without time, instead of the old java Date class
    firstRegistration: LocalDate
)
