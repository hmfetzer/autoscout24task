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
        db.update(ad1)
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
      db.save(ad1) map (_ mustBe ad1)
    }
    " not enable adding an ad twice" in {
      val db = testDb
      // with a for comprehension the futures run sequentially
      for {
        _ <- db.save(ad1) map (_ mustBe ad1)
        _ <- recoverToSucceededIf[Throwable] {
          db.save(ad1)
        }
      } yield succeed
    }
    "contain an element after adding" in {
      val db = testDb
      // with a for comprehension the futures run sequentially
      for {
        _ <- db.save(ad1) map { _ mustBe ad1 }
        _ <- db.getOne(ad1.id) map { _ mustBe ad1 }
      } yield succeed
    }

    " enable deleting an element" in {
      val db = testDb
      // with a for comprehension the futures run sequentially
      for {
        _ <- db.save(ad1) map { _ mustBe ad1 }
        _ <- db.delete(ad1.id) map { _ mustBe ad1 }
        // after deleting db should be empty
        _ <- db.getAll(None) map { _ mustBe List() }
      } yield succeed
    }

    " update an existing element " in {
      val db = testDb
      // with a for comprehension the futures run sequentially
      for {
        _ <- db.save(ad1) map { _ mustBe ad1 }
        newTitle = "new Title"
        ad2 = ad1.copy(title = newTitle)
        // update should return the new element
        _ <- db.update(ad2) map { _.title mustBe newTitle }
        // reading from db should also return new element
        _ <- db.getOne(ad2.id) map { _.title mustBe newTitle }
      } yield succeed
    }

  }

}
