package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Helpers._
import play.api.test._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */

class ApplicationSpec extends PlaySpec with GuiceOneAppPerTest {

  "Application" should {

    "send 404 on a request to undefined url" in {
      val boum = route(app, FakeRequest(GET, "/boum")).get

      status(boum) must equal(NOT_FOUND)
    }

    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/")).get

      status(home) must equal(OK)
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("lobortis.tellus@temporarcu.com")
      contentAsString(home) must include ("pellentesque.tellus@atvelit.com")
    }

    "render the index page with page parameter" in {
      val home = route(app, FakeRequest(GET, "/?page=7&size=30&key=email&dir=asc")).get

      status(home) must equal(OK)
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("dictum.ultricies.ligula@consequatenim.org")
    }

  }
}
