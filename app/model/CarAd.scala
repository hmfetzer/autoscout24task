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

sealed trait Fuel
case object Gasoline extends Fuel { override def toString = "gasoline" }
case object Diesel extends Fuel { override def toString = "diesel" }

object Fuel {
  def apply(f: String): Fuel = f match {
    case "diesel"   => Diesel
    case "gasoline" => Gasoline
    case _ =>
      throw new IllegalArgumentException(
        s"No known fuel: '$f', expected 'gasoline' or 'diesel'"
      )
  }
}

sealed trait CarAd {
  val id: Int
  val title: String
  val fuel: Fuel
  val price: Int
}

case class NewCarAd(
    id: Int,
    title: String,
    fuel: Fuel,
    price: Int
) extends CarAd

case class UsedCarAd(
    id: Int,
    title: String,
    fuel: Fuel,
    price: Int,
    mileage: Int,
    firstRegistration: LocalDate
) extends CarAd
