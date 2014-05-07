package models

import play.api.libs.json._
import play.api.libs.functional.syntax._



/**
 * Created by henry.goldwire on 5/5/14.
 */
case class Battery(state: String, level: Int)


object Battery {

  implicit val locationWrites: Writes[Battery] = (
    (JsPath \ "state").write[String] and
      (JsPath \ "level").write[Int]
    )(unlift(Battery.unapply))

  implicit val locationReads: Reads[Battery] = (
    (JsPath \ "state").read[String] and
      ((JsPath \ 'batteryLevel).read[Int] orElse (__ \ 'batteryLevel).read[String].map(_.toInt))
    )(Battery.apply _)
}