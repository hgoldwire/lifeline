package json


import models._
import org.joda.time.DateTime
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatestplus.play.PlaySpec
import play.api.libs.json._


@RunWith(classOf[JUnitRunner])
class PulseSpec extends PlaySpec {

  var location1 = Location(20.0, -20.0, 10, 10, 10)
  var location2 = Location(20.5, -20.5, 11, 20, 20)
  var location3 = Location(21.0, -21.0, 12, 30, 30)
  var location4 = Location(21.5, -21.5, 13, 40, 40)

  var motion1 = Motion(0, false, false, false)
  var motion2 = Motion(3, true, false, false)
  var motion3 = Motion(-1, true, true, false)
  var motion4 = Motion(1, false, false, false)

  var battery1 = Battery(Charging(), BatteryLevel(60))
  var battery2 = Battery(Unplugged(), BatteryLevel(65))
  var battery3 = Battery(Full(), BatteryLevel(100))
  var battery4 = Battery(Unknown(), BatteryLevel(95))

  val datetime1 = new DateTime(1397222000000L)
  val datetime2 = new DateTime(1397222300000L)
  val datetime3 = new DateTime(1397222600000L)
  val datetime4 = new DateTime(1397222900000L)

  val sudid = "F4D4D3F1-9999-9999-9999-B0A88B846623"
  val deviceName = "mochaPhone"

  var pulse1 = Pulse(datetime1, sudid, deviceName, location1, battery1, motion1)
  var pulse2 = Pulse(datetime2, sudid, deviceName, location2, battery2, motion2)
  var pulse3 = Pulse(datetime3, sudid, deviceName, location3, battery3, motion3)
  var pulse4 = Pulse(datetime4, sudid, deviceName, location4, battery4, motion4)

  val pulses = Seq(pulse1, pulse2, pulse3, pulse4)

  "IntervalBucketizer.mergePulses" must {
    "return expected result for two canned pulses" in {
      var expectedLocation = Location(20.25, -20.25, 10, 15, 15)
      var expectedMotion = Motion(3, true, false, false)
      var expectedBattery = Battery(Unplugged(), BatteryLevel(62))
      var expectedDateTime = new DateTime(pulses.slice(0, 2).map(_.datetime.getMillis).sum / 2)
      var expectedPulse = Pulse(expectedDateTime, sudid, deviceName, expectedLocation, expectedBattery, expectedMotion)

      val merged: Pulse = IntervalBucketizer.mergePulses(pulses.slice(0, 2))
      merged mustBe expectedPulse
    }
    "return expected result for four canned pulses" in {
      var expectedLocation = Location(20.75, -20.75, 11, 25, 25)
      var expectedMotion = Motion(3, true, true, false)
      var expectedBattery = Battery(Unplugged(), BatteryLevel(80))
      var expectedDateTime = new DateTime(pulses.map(_.datetime.getMillis).sum / 4)
      var expectedPulse = Pulse(expectedDateTime, sudid, deviceName, expectedLocation, expectedBattery, expectedMotion)

      val merged: Pulse = IntervalBucketizer.mergePulses(pulses)
      merged mustBe expectedPulse
    }
  }
  "IntervalBucketizer.bucketize" must {
    "return original data when interval same as source data (300s)" in {
      val bucketized = IntervalBucketizer.bucketize(300, pulses)
      bucketized mustBe pulses
    }

    "should contain two values when bucketInterval is 600s" in {
      val bucketized = IntervalBucketizer.bucketize(600, pulses)
      bucketized.length mustBe 2
    }
    "return expected result for four canned pulses" in {
      val bucketizeLoc1 = Location(20.25, -20.25, 10, 15, 15)
      val bucketizeLoc2 = Location(21.25, -21.25, 12, 35, 35)
      var bucketizeMotion1 = Motion(3, true, false, false)
      var bucketizeMotion2 = Motion(1, true, true, false)
      var bucketizeBattery1 = Battery(Unplugged(), BatteryLevel(62))
      var bucketizeBattery2 = Battery(Full(), BatteryLevel(97))
      var bucketizeDateTime1 = new DateTime(1397222150000L)
      var bucketizeDateTime2 = new DateTime(1397222750000L)
      var bucketizePulse1 = Pulse(bucketizeDateTime1, sudid, deviceName, bucketizeLoc1, bucketizeBattery1, bucketizeMotion1)
      var bucketizePulse2 = Pulse(bucketizeDateTime2, sudid, deviceName, bucketizeLoc2, bucketizeBattery2, bucketizeMotion2)

      val expectedPulses = Seq(bucketizePulse1, bucketizePulse2)
      val mergedPulses = IntervalBucketizer.bucketize(600, pulses)

      mergedPulses mustBe expectedPulses
    }
  }
}

