package db

// Testing the Dummy-DB
class H2DbSpec extends CarAdDBSpec {
  override def initDB = new CarAdH2DAO
}
