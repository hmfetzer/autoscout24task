package db

// Testing the Dummy-DB
class DummyDbSpec extends CarAdDBSpec {
  override def testDb = new CarAdDummyDAO
}
