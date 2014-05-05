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

  /*
    * transform JSON like
      {
        "speed": 0,
        "batteryState": "charging",
        "sudid": "F4D4D3F1-9999-9999-9999-B0A88B846623",
        "timestamp": 1397222000,
        "motion": [],
        "longitude": -20.0,
        "horizontalAccuracy": 10,
        "latitude": 20.0,
        "verticalAccuracy": 10,
        "batteryLevel": "60",
        "altitude": 10,
        "deviceName": "mochaPhone",
        "createdAt": 1397222000000,
        "updatedAt": 1397222000000,
        "_id": "5347ec1cc311731e21c75ce0"
      }
      into
      {
        "timestamp": 1397222000,
        "_id": "5347ec1cc311731e21c75ce0",
        "sudid": "F4D4D3F1-9999-9999-9999-B0A88B846623",
        "deviceName": "mochaPhone",
        "createdAt": 1397222000000,
        "updatedAt": 1397222000000,
        "speed": 0,
        "motion": [],
        "location": {
          "longitude": -20.0,
          "latitude": 20.0,
          "horizontalAccuracy": 10,
          "verticalAccuracy": 10,
          "altitude": 10
        },
        "battery": {
          "batteryLevel": "60",
          "batteryState": "charging"
        }
      }

  }*/

  val pulse = Json.obj(
    "speed" -> 0,
    "batteryState" -> "charging",
    "sudid" -> "F4D4D3F1-9999-9999-9999-B0A88B846623",
    "timestamp" -> 1397222000,
    "motion" -> Json.arr(),
    "longitude" -> -20.0,
    "horizontalAccuracy" -> 10,
    "latitude" -> 20.0,
    "verticalAccuracy" -> 10,
    "batteryLevel" -> "60",
    "altitude" -> 10,
    "deviceName" -> "mochaPhone",
    "createdAt" -> 1397222000,
    "updatedAt" -> 1397220000,
    "_id" -> "5347ec1cc311731e21c75ce0"
  )

  val pulse2model = (
    (__ \ 'altitude).json.prune andThen
      (__ \ 'latitude).json.prune andThen
      (__ \ 'longitude).json.prune andThen
      (__ \ 'horizontalAccuracy).json.prune andThen
      (__ \ 'verticalAccuracy).json.prune
    )


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

