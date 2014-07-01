package json


import models.Motion
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatestplus.play.PlaySpec
import play.api.libs.json._

@RunWith(classOf[JUnitRunner])
class MotionJsonSpec extends PlaySpec {

  var expectedMotion = Motion(3, true, true, false)

  var expectedJsObject = JsObject(Seq(
    ("speed", JsNumber(3)),
    ("isWalking", JsBoolean(true)),
    ("isDriving", JsBoolean(false)),
    ("isRunning", JsBoolean(true))
  ))

  "A Motion" must {
    "serialize to JSON as expected" in {
      val json = Json.toJson(expectedMotion)
      json mustBe expectedJsObject
      //      println(Json.prettyPrint(json))
    }

    "deserialize from JSON as expected" in {
      val loc = Json.parse(Json.prettyPrint(expectedJsObject)).validate[Motion]
      loc.asOpt mustEqual (Some(expectedMotion))

      loc.fold(errors => {
        //        println(errors)
      }, location => {
        //        println(location)
      })
    }
  }
}
