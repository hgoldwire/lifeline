package models

import play.api.libs.json._ // JSON library
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax


/**
* Created by henry.goldwire on 5/5/14.
*/
case class Pulse(guid: String, deviceName: String, location: Option[Location])

object Pulse {

  implicit val pulseWrites: Writes[Pulse] = (
    (JsPath \ "deviceName").write[String] and
      (JsPath \ "guid").write[String] and
      (JsPath \ "location").writeNullable[Location]
    )(unlift(Pulse.unapply))

  implicit val pulseReads: Reads[Pulse] = (
    (JsPath \ "deviceName").read[String] and
      (JsPath \ "guid").read[String] and
      (JsPath \ "location").readNullable[Location]
    )(Pulse.apply _)

  var locationList: List[Location] = {
    List(
      Location(1.2, 2.3),
      Location(4.5, 5.6)
    )
  }

  var list: List[Pulse] = {
    List(
      Pulse("device1", "guid1", Some(locationList(0))),
      Pulse("device2", "guid2", None)
    )
  }

  def save(pulse: Pulse) = {
    list = list ::: List(pulse)
  }
}

