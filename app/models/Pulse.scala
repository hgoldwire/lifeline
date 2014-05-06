package models

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.db.slick.Config.driver.simple._

/**
 * Created by henry.goldwire on 5/5/14.
 */
case class Pulse(sudid: String, deviceName: String, location: Location, battery: Battery)


object Pulse {

  // helper for when location data isn't nested in location field
  val unnestedLocationReads: Reads[Location] = (
    (JsPath \ 'latitude).read[Double] and
      (JsPath \ 'longitude).read[Double] and
      (JsPath \ 'altitude).read[Int] and
      (JsPath \ 'horizontalAccuracy).read[Int] and
      (JsPath \ 'verticalAccuracy).read[Int]
    )(Location.apply _)

  // helper for when battery data isn't nested in battery field
  val unnestedBatteryReads: Reads[Battery] = (
    (JsPath \ 'batteryState).read[String] and
      ((JsPath \ 'batteryLevel).read[Int] orElse (__ \ 'batteryLevel).read[String].map(_.toInt))
    )(Battery.apply _)

  val pulseReads: Reads[Pulse] = (
    (JsPath \ 'deviceName).read[String] and
      (JsPath \ 'sudid).read[String] and
      ((JsPath \ 'location).read[Location] orElse (unnestedLocationReads)) and
      ((JsPath \ 'battery).read[Battery] orElse (unnestedBatteryReads))
    )(Pulse.apply _)

  implicit val pulseWrites: Writes[Pulse] = (
    (JsPath \ 'deviceName).write[String] and
      (JsPath \ 'sudid).write[String] and
      (JsPath \ 'location).write[Location] and
      (JsPath \ 'battery).write[Battery]
    )(unlift(Pulse.unapply))
}

class PulsesTable(tag: Tag) extends Table[Pulse](tag, "PULSE") {

  def sudid = column[String]("guid")

  def deviceName = column[String]("device_name")

  def latitude = column[Double]("latitude")

  def longitude = column[Double]("longitude")

  def altitude = column[Int]("altitude")

  def horizontalAccuracy = column[Int]("horizontal_accuracy")

  def verticalAccuracy = column[Int]("vertical_accuracy")

  def batteryState = column[String]("battery_state")

  def batteryLevel = column[Int]("battery_level")

  def battery = (batteryState, batteryLevel) <>((Battery.apply _).tupled, Battery.unapply _)

  def location = (latitude, longitude, altitude, horizontalAccuracy, verticalAccuracy) <>((Location.apply _).tupled, Location.unapply _)

  def * = (sudid, deviceName, location, battery) <>((Pulse.apply _).tupled, Pulse.unapply _)
}

