
import models._
import org.joda.time.DateTime
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatestplus.play.PlaySpec
import play.api.libs.json._

@RunWith(classOf[JUnitRunner])
class PulseSpec extends PlaySpec {

  var jsonString =
    """
      | {
      |   "sudid": "F4D4D3F1-9999-9999-9999-B0A88B846623",
      |   "timestamp": 1397222150,
      |   "deviceName": "mochaPhone",
      |   "motion": {
      |     "speed": 3,
      |     "isWalking": true,
      |     "isDriving": false,
      |     "isRunning": true
      |    },
      |    "location": {
      |      "longitude": -73.954239534283,
      |      "horizontalAccuracy": 50,
      |      "altitude": 29,
      |      "latitude": 40.73021636348285,
      |      "verticalAccuracy": 64
      |    },
      |   "battery": {
      |     "state": "charging",
      |     "level": 60
      |   }
      | }
    """.stripMargin

  val expectedJsValue = Json.parse(jsonString)

  var expectedLocation = Location(40.73021636348285, -73.954239534283, 29, 50, 64)
  var expectedMotion = Motion(3, true, true, false)
  var expectedBattery = Battery(Charging(), BatteryLevel(60))
  var expectedPulse = Pulse(new DateTime(1397222150 * 1000L), "F4D4D3F1-9999-9999-9999-B0A88B846623", "mochaPhone", expectedLocation, expectedBattery, expectedMotion)

//  val json1 = Json.toJson(expectedPulse)
//  val json2 = expectedJsValue
//  println(Json.prettyPrint(json1))
//  println(Json.prettyPrint(json2))


  "A Pulse" must {
    "serialize to JSON as expected" in {
      val jsVal = Json.toJson(expectedPulse)
      jsVal mustBe expectedJsValue
      //      println(Json.prettyPrint(json))
    }

    "deserialize from JSON as expected" in {
      println("expectedJsValue: " + expectedJsValue)
      val pulse = expectedJsValue.validate[Pulse]
      println("expectedPulse: " + expectedPulse)
      println("pulse: " + pulse)
      pulse.asOpt mustEqual (Some(expectedPulse))

      pulse.fold(errors => {
        //        println(errors)
      }, pulse => {
        //        println(location)
      })
    }
  }
}
