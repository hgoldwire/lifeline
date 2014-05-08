package models

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.db.slick.Config.driver.simple._
import play.api.data.validation.ValidationError
import models.Motion._
import org.joda.time.DateTime
import com.github.tototoshi.slick.MySQLJodaSupport._


/**
 * Created by henry.goldwire on 5/5/14.
 */
case class Pulse(datetime: DateTime, sudid: String, deviceName: String, location: Location, battery: Battery, motion: Motion)

object Pulse {

  // helper to get unix timestamps into joda DateTime format (i.e. * 1000ms)
  object timestamp2DateTime extends Reads[DateTime] {
    def reads(json: JsValue) = {
      val timestamp = json.validate[Int].getOrElse(0)
      JsSuccess(new DateTime(timestamp * 1000L))
    }
  }

  // helper for when location data isn't nested in location field
  object unnestedLocationReads extends Reads[Location] {
    def reads(json: JsValue) = {
      val latitude = (json \ "latitude").validate[Double].getOrElse(0: Double)
      val longitude = (json \ "longitude").validate[Double].getOrElse(0: Double)
      val altitude = (json \ "altitude").validate[Int].getOrElse(0)
      val horizontalAccuracy = (json \ "horizontalAccuracy").validate[Int].getOrElse(0)
      val verticalAccuracy = (json \ "verticalAccuracy").validate[Int].getOrElse(0)
      val location = Location(latitude, longitude, altitude, horizontalAccuracy, verticalAccuracy)
      JsSuccess(location)
    }
  }

  // helper for when speed and motion are separate fields
  implicit object unnestedMotionReads extends Reads[Motion] {
    def reads(json: JsValue) = {
      def speed = (json \ "speed").validate[Int].getOrElse(-1)
      (json \ "motion").validate[Seq[String]].fold(errors => {
        JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jsarray"))))
      }, motions => {
        val isWalking = motions.contains("walking")
        val isRunning = motions.contains("running")
        val isDriving = motions.contains("driving")
        JsSuccess(Motion(speed, isWalking, isRunning, isDriving))
      })
    }
  }

  // helper for when battery data isn't nested in battery field
  val unnestedBatteryReads: Reads[Battery] = (
    (JsPath \ 'batteryState).read[String] and
      ((JsPath \ 'batteryLevel).read[Int] orElse (__ \ 'batteryLevel).read[String].map(_.toInt))
    )(Battery.apply _)

  implicit val pulseReads: Reads[Pulse] = (
    (JsPath \ 'timestamp).read[DateTime](timestamp2DateTime) and
      (JsPath \ 'sudid).read[String] and
      (JsPath \ 'deviceName).read[String] and
      ((JsPath \ 'location).read[Location] orElse (unnestedLocationReads)) and
      ((JsPath \ 'battery).read[Battery] orElse (unnestedBatteryReads)) and
      (JsPath \ 'motion).read[Motion](motionReads).orElse(JsPath.read[Motion](unnestedMotionReads))
    )(Pulse.apply _)

  implicit val pulseWrites: Writes[Pulse] = (
    (JsPath \ 'timestamp).write[DateTime] and
      (JsPath \ 'sudid).write[String] and
      (JsPath \ 'deviceName).write[String] and
      (JsPath \ 'location).write[Location] and
      (JsPath \ 'battery).write[Battery] and
      (JsPath \ 'motion).write[Motion](motionWrites)
    )(unlift(Pulse.unapply))

 /*
 * merges multiple pulses according to these rules:
 *   speed: highest value
 *   batteryState: first value that appears in the sequence ["unplugged", "charging", "full", "unknown"]
 *   sudid: no change
 *   timestamp: average of values
 *   motion: include all unique values
 *   longitude: average of values
 *   horizontalAccuracy: average of values
 *   latitude: average of values
 *   verticalAccuracy: average of values
 *   batteryLevel: average of values
 *   altitude: average of values
 *   deviceName: no change
 *   createdAt: REMOVED
 *   updatedAt: REMOVED
 *   id: REMOVED
 */
  def mergePulses(pulses: Seq[Pulse]) = {
//    var speed  pulses.map(_.speed).max
  }
}

class PulsesTable(tag: Tag) extends Table[Pulse](tag, "PULSE") {

  def datetime = column[DateTime]("datetime")

  def sudid = column[String]("guid")

  def deviceName = column[String]("device_name")

  def latitude = column[Double]("latitude")

  def longitude = column[Double]("longitude")

  def altitude = column[Int]("altitude")

  def horizontalAccuracy = column[Int]("horizontal_accuracy")

  def verticalAccuracy = column[Int]("vertical_accuracy")

  def batteryState = column[String]("battery_state")

  def batteryLevel = column[Int]("battery_level")

  def motionSpeed = column[Int]("motion_speed")

  def motionWalking = column[Boolean]("motion_walking")

  def motionRunning = column[Boolean]("motion_running")

  def motionDriving = column[Boolean]("motion_driving")

  def motion = (motionSpeed, motionWalking, motionRunning, motionDriving) <>((Motion.apply _).tupled, Motion.unapply _)

  def battery = (batteryState, batteryLevel) <>((Battery.apply _).tupled, Battery.unapply _)

  def location = (latitude, longitude, altitude, horizontalAccuracy, verticalAccuracy) <>((Location.apply _).tupled, Location.unapply _)

  def * = (datetime, sudid, deviceName, location, battery, motion) <>((Pulse.apply _).tupled, Pulse.unapply _)
}

