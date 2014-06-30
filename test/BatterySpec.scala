
package test

import collection.mutable.Stack
import org.scalatestplus.play.PlaySpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import models.{BatteryLevel, Charging, Battery}
import play.api.libs.json.{JsResult, Json}

@RunWith(classOf[JUnitRunner])
class BatterySpec extends PlaySpec {

  var expectedBattery = Battery(Charging(), BatteryLevel(60))
  var expectedJson = """{"state":"charging","level":60}"""

  "A Battery" must {
    "serialize to JSON as expected" in {
      val json = Json.toJson(expectedBattery)
      json.toString mustBe expectedJson
      //      println(Json.prettyPrint(json))

    }

    "deserialize from JSON as expected" in {
      val b = Json.parse(expectedJson).validate[Battery]
      b.asOpt mustEqual (Some(expectedBattery))

      b.fold(errors => {
        println(errors)
      }, battery => {
        //        println(battery)
      })
    }
  }
}
