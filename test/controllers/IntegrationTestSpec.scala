package controllers

import play.api.test.Helpers._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting
import db.CarAdH2DAO
import play.api.test.FakeRequest
import play.api.libs.json._
import db.TestData
import play.mvc.BodyParser.AnyContent
import com.fasterxml.jackson.databind.JsonNode
import play.api.libs.json.JsValue

class IntegrationTestSpec
    extends PlaySpec
    with GuiceOneAppPerTest
    with Injecting
    with TestData {

  "The CarAd-REST-Service" should {

    "start with an empty db without car ads " in {
      val res1 = route(app, FakeRequest(GET, "/v1/carads")).get
      status(res1) mustBe OK
      contentType(res1) mustBe Some("application/json")
      // contentAsJson(res) mustBe Json.parse("[]") didn't work!
      contentAsJson(res1).toString mustBe Json.parse("[]").toString

      val res2 = route(app, FakeRequest(GET, "/v1/carads/1")).get
      status(res2) mustBe NOT_FOUND
      contentType(res2) mustBe Some("text/plain")
    }

    "after inserting an car ad, return it" in {
      val jsCarAd = Json.parse(usedAdJson)
      val req = FakeRequest(POST, "/v1/carads").withJsonBody(jsCarAd)
      val res0 = route(app, req).get
      status(res0) mustBe OK
      contentType(res0) mustBe Some("application/json")
      contentAsJson(res0).toString mustBe Json.parse(usedAdJson).toString

      val res1 = route(app, FakeRequest(GET, "/v1/carads")).get
      status(res1) mustBe OK
      contentType(res1) mustBe Some("application/json")
      contentAsJson(res1).toString mustBe "[" + jsCarAd.toString + "]"

      val res2 = route(app, FakeRequest(GET, "/v1/carads/1")).get
      status(res2) mustBe OK
      contentType(res2) mustBe Some("application/json")
      contentAsJson(res2).toString mustBe jsCarAd.toString
    }

    "after inserting and deleting an ad, be empty again" in {
      val jsCarAd = Json.parse(usedAdJson)
      val req = FakeRequest(POST, "/v1/carads").withJsonBody(jsCarAd)
      val res0 = route(app, req).get
      info(res0.value.toString)
      status(res0) mustBe OK
      contentType(res0) mustBe Some("application/json")
      contentAsJson(res0).toString mustBe Json.parse(usedAdJson).toString

      val res1 = route(app, FakeRequest(GET, "/v1/carads")).get
      status(res1) mustBe OK
      contentType(res1) mustBe Some("application/json")
      contentAsJson(res1).toString mustBe "[" + jsCarAd.toString + "]"

      val res2 = route(app, FakeRequest(GET, "/v1/carads/1")).get
      status(res2) mustBe OK
      contentType(res2) mustBe Some("application/json")
      contentAsJson(res2).toString mustBe jsCarAd.toString

      val res3 = route(app, FakeRequest(DELETE, "/v1/carads/1")).get
      status(res2) mustBe OK
      contentType(res2) mustBe Some("application/json")
      contentAsJson(res2).toString mustBe jsCarAd.toString

      val res4 = route(app, FakeRequest(GET, "/v1/carads")).get
      status(res4) mustBe OK
      contentType(res4) mustBe Some("application/json")
      contentAsJson(res4).toString mustBe Json.parse("[]").toString

      val res5 = route(app, FakeRequest(GET, "/v1/carads/1")).get
      status(res5) mustBe NOT_FOUND
      contentType(res5) mustBe Some("text/plain")
    }

    ", for a real production system, have more tests!!!" in {}
  }
}
