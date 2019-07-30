package db

import model.CarAd
import org.scalatest.AsyncWordSpec
import org.scalatest.MustMatchers

// some quick tests that could be performed on any DB/DAO instance
trait CarAdDBSpec extends AsyncWordSpec with MustMatchers with TestData {

  def testDb: CarAdDAO

  "The DB " should {

    "initially be empty " in {
      val db = testDb
      db.getAll(None) map { _ mustBe List() }
    }

    "initially not have an carAd with id 1" in {
      recoverToSucceededIf[NoSuchElementException] {
        val db = testDb
        db.getOne(1)
      }
    }

    "initially not enable update for carAd with id 1" in {
      recoverToSucceededIf[NoSuchElementException] {
        val db = testDb
        db.update(usedAd)
      }
    }
    "initially not delete a carAd with id 1" in {
      recoverToSucceededIf[NoSuchElementException] {
        val db = testDb
        db.delete(1)
      }
    }

    " enable adding an ad " in {
      val db = testDb
      db.save(usedAd) map (_ mustBe usedAd)
    }
    " not enable adding an ad twice" in {
      val db = testDb
      // with a for comprehension the futures run sequentially
      for {
        _ <- db.save(usedAd) map (_ mustBe usedAd)
        _ <- recoverToSucceededIf[Throwable] {
          db.save(usedAd)
        }
      } yield succeed
    }
    "contain an element after adding" in {
      val db = testDb
      // with a for comprehension the futures run sequentially
      for {
        _ <- db.save(usedAd) map { _ mustBe usedAd }
        _ <- db.getOne(usedAd.id) map { _ mustBe usedAd }
      } yield succeed
    }

    " enable deleting an element" in {
      val db = testDb
      // with a for comprehension the futures run sequentially
      for {
        _ <- db.save(usedAd) map { _ mustBe usedAd }
        _ <- db.delete(usedAd.id) map { _ mustBe usedAd }
        // after deleting db should be empty
        _ <- db.getAll(None) map { _ mustBe List() }
      } yield succeed
    }

    " update an existing element " in {
      val db = testDb
      // with a for comprehension the futures run sequentially
      for {
        _ <- db.save(usedAd) map { _ mustBe usedAd }
        newTitle = "new Title"
        newAd = usedAd.copy(title = newTitle)
        // update should return the new element
        _ <- db.update(newAd) map { _.title mustBe newTitle }
        // reading from db should also return new element
        _ <- db.getOne(newAd.id) map { _.title mustBe newTitle }
      } yield succeed
    }

  }

}
