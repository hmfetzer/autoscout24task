package db;
import model.CarAd
import org.joda.time.LocalDate

trait TestData {

  val ad1 = CarAd(
    id = 1,
    title = "TestAd 1",
    fuel = "diesel",
    price = 2,
    newCar = false,
    mileage = Some(3),
    firstRegistration = Some(new LocalDate(2015, 2, 20))
  )

  val ad1Json = """ {
      "id": 1,
      "title": "TestAd 1",
      "fuel":  "diesel",
      "price": 2,
      "newCar": false,
      "mileage": 3,
      "firstRegistration": "2015-02-20"
      }  """

  val ad2 = CarAd(
    id = 2,
    title = "TestAd 2",
    fuel = "diesel",
    price = 22,
    newCar = true,
    mileage = None,
    firstRegistration = None
  )

  val ad2Json = """ {
          "id": 2,
          "title": "TestAd 2",
          "fuel":  "diesel",
          "price": 22,
          "newCar": true,
          "mileage": null,
          "firstRegistration": null
          }  """

}
