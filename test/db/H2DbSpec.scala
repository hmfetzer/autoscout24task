package db

// Testing the Dummy-DB
class H2DbSpec extends CarAdDBSpec {
  override def testDb = {
    val db = new CarAdH2DAO()
    db.init
    db
  }

}
