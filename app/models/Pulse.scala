package models

import play.api.libs.json._ // JSON library
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax


/**
* Created by henry.goldwire on 5/5/14.
*/
case class Pulse(deviceName: String, sudid: String, location: Location)

object Pulse {

  implicit val pulseWrites: Writes[Pulse] = (
    (JsPath \ "deviceName").write[String] and
      (JsPath \ "sudid").write[String] and
      (JsPath \ "location").write[Location]
    )(unlift(Pulse.unapply))

  implicit val pulseReads: Reads[Pulse] = (
    (JsPath \ "deviceName").read[String] and
      (JsPath \ "sudid").read[String] and
      (JsPath \ "location").read[Location]
    )(Pulse.apply _)

//  implicit val pulseReads: Reads[Pulse] = (JsPath \ "deviceName").read[String] and (JsPath \ "sudid").read[String]

  var locationList: List[Location] = {
    List(
      Location(1.2, 2.3),
      Location(4.5, 5.6)
    )
  }

  var list: List[Pulse] = {
    List(
      Pulse("device1", "sudid1", locationList(0)),
      Pulse("device2", "sudid2", locationList(1))
    )
  }

  def save(pulse: Pulse) = {
    list = list ::: List(pulse)
  }
}

