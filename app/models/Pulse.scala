package models

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.db.slick.Config.driver.simple._

/**
 * Created by henry.goldwire on 5/5/14.
 */
case class Pulse(guid: String, deviceName: String, location: Location)


object Pulse {

  implicit val pulseWrites: Writes[Pulse] = (
    (JsPath \ "deviceName").write[String] and
      (JsPath \ "guid").write[String] and
      (JsPath \ "location").write[Location]
    )(unlift(Pulse.unapply))

  implicit val pulseReads: Reads[Pulse] = (
    (JsPath \ "deviceName").read[String] and
      (JsPath \ "guid").read[String] and
      (JsPath \ "location").read[Location]
    )(Pulse.apply _)
}

class PulsesTable(tag: Tag) extends Table[Pulse](tag, "PULSE") {

  def guid = column[String]("guid", O.PrimaryKey)

  def deviceName = column[String]("device_name")

  def latitude = column[Double]("latitude")

  def longitude = column[Double]("longitude")

  def location = (latitude, longitude) <>((Location.apply _).tupled, Location.unapply _)

  def * = (guid, deviceName, location) <>((Pulse.apply _).tupled, Pulse.unapply _)
}
