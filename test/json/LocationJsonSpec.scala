package json


import models.Location
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatestplus.play.PlaySpec
import play.api.libs.json._

@RunWith(classOf[JUnitRunner])
class LocationJsonSpec extends PlaySpec {

  var expectedLocation = Location(40.73021636348285, -73.954239534283, 29, 50, 64)

  var expectedJsObject = JsObject(Seq(
    ("latitude", JsNumber(40.73021636348285)),
    ("longitude", JsNumber(-73.954239534283)),
    ("altitude",JsNumber(29)),
    ("horizontalAccuracy",JsNumber(50)),
    ("verticalAccuracy", JsNumber(64))
  ))

  "A Location" must {
    "serialize to JSON as expected" in {
      val json = Json.toJson(expectedLocation)
      json mustBe expectedJsObject
//      println(Json.prettyPrint(json))
    }

    "deserialize from JSON as expected" in {
      val loc = Json.parse(Json.prettyPrint(expectedJsObject)).validate[Location]
      loc.asOpt mustEqual (Some(expectedLocation))

      loc.fold(errors => {
//        println(errors)
      }, location => {
//        println(location)
      })
    }
  }
}
