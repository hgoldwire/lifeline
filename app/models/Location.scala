package models

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._


/**
 * Created by henry.goldwire on 5/5/14.
 */
case class Location(latitude: Double, longitude: Double, altitude: Int, horizontalAccuracy: Int, verticalAccuracy: Int)

object Location {

  object ReadsZero extends Reads[Int] {
    def reads(json: JsValue) = json.validate[Int]
  }

  implicit val locationWrites: Writes[Location] = (
    (JsPath \ "latitude").write[Double] and
      (JsPath \ "longitude").write[Double] and
      (JsPath \ "altitude").write[Int] and
      (JsPath \ "horizontalAccuracy").write[Int] and
      (JsPath \ "verticalAccuracy").write[Int]
    )(unlift(Location.unapply))

  implicit val locationReads: Reads[Location] = (
    (JsPath \ "latitude").read[Double] and
      (JsPath \ "longitude").read[Double] and
      (JsPath \ "altitude").read[Int] and
      (JsPath \ "horizontalAccuracy").read[Int] and
      (JsPath \ "verticalAccuracy").read[Int]
    )(Location.apply _)
}



