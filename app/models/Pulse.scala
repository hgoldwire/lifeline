package models

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.db.slick.Config.driver.simple._

/**
 * Created by henry.goldwire on 5/5/14.
 */
case class Pulse(sudid: String, deviceName: String, location: Location)


object Pulse {

  val normalPulseReads: Reads[Pulse] = (
    (JsPath \ "deviceName").read[String] and
      (JsPath \ "sudid").read[String] and
      (JsPath \ "latitude").read[Double] and
      (JsPath \ "longitude").read[Double] and
      (JsPath \ "altitude").read[Int] and
      (JsPath \ "horizontalAccuracy").read[Int] and
      (JsPath \ "verticalAccuracy").read[Int]
    )((deviceName, sudid, latitude, longitude, altitude, horizontalAccuracy, verticalAccuracy) => Pulse(deviceName, sudid, Location(latitude, longitude, altitude, horizontalAccuracy, verticalAccuracy)))

  val nestedPulseReads: Reads[Pulse] = (
    (JsPath \ "deviceName").read[String] and
      (JsPath \ "sudid").read[String] and
      (JsPath \ "location").read[Location]
    )(Pulse.apply _)

  implicit val pulseWrites: Writes[Pulse] = (
    (JsPath \ "deviceName").write[String] and
      (JsPath \ "sudid").write[String] and
      (JsPath \ "location").write[Location]
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

  def location = (latitude, longitude, altitude, horizontalAccuracy, verticalAccuracy) <>((Location.apply _).tupled, Location.unapply _)

  def * = (sudid, deviceName, location) <>((Pulse.apply _).tupled, Pulse.unapply _)
}

