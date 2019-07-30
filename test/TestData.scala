package db;
import model._
import org.joda.time.LocalDate

trait TestData {

  val usedAd = UsedCarAd(
    id = 1,
    title = "Used ad with id 1",
    fuel = Diesel,
    price = 2,
    mileage = 3,
    firstRegistration = new LocalDate(2015, 2, 20)
  )

  val usedAdJson = """ {
      "id": 1,
      "title": "TestAd 1",
      "fuel":  "diesel",
      "price": 2,
      "mileage": 3,
      "firstRegistration": "2015-02-20"
      }  """

  val newAd = NewCarAd(
    id = 2,
    title = "New ad with id 2",
    fuel = Gasoline,
    price = 22
  )

  val newAd2 = NewCarAd(
    id = 3,
    title = "An other new ad with id 3",
    fuel = Diesel,
    price = 22
  )
  val newAdJson = """ {
          "id": 2,
          "title": "TestAd 2",
          "fuel":  "diesel",
          "price": 22,
          }  """

}
