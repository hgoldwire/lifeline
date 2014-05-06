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

  implicit val pulseWrites: Writes[Pulse] = (
    (JsPath \ "deviceName").write[String] and
      (JsPath \ "sudid").write[String] and
      (JsPath \ "location").write[Location]
    )(unlift(Pulse.unapply))

  implicit val pulseReads: Reads[Pulse] = (
    (JsPath \ "deviceName").read[String] and
      (JsPath \ "sudid").read[String] and
      (__).read[Location]
    )(Pulse.apply _)
}

class PulsesTable(tag: Tag) extends Table[Pulse](tag, "PULSE") {

  def sudid = column[String]("guid", O.PrimaryKey)

  def deviceName = column[String]("device_name")

  def latitude = column[Double]("latitude")

  def longitude = column[Double]("longitude")

  def altitude = column[Int]("altitude")

  def horizontalAccuracy = column[Int]("horizontal_accuracy")

  def verticalAccuracy = column[Int]("vertical_accuracy")

  def location = (latitude, longitude, altitude, horizontalAccuracy, verticalAccuracy) <>((Location.apply _).tupled, Location.unapply _)

  def * = (sudid, deviceName, location) <>((Pulse.apply _).tupled, Pulse.unapply _)
}

