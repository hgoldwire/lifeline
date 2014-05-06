package controllers

import models._
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Json._


/**
 * Created by henry.goldwire on 5/5/14.
 */
object PulseController extends Controller {

  val Pulses = TableQuery[PulsesTable] //see a way to architect your app in the computers-database-slick sample

  def listPulses = DBAction {
    implicit rs =>
      Ok(toJson(Pulses.list))
  }

  implicit val normalPulseReads: Reads[Pulse] = (
    (JsPath \ "deviceName").read[String] and
      (JsPath \ "sudid").read[String] and
      (JsPath \ "latitude").read[Double] and
      (JsPath \ "longitude").read[Double] and
      (JsPath \ "altitude").read[Int] and
      (JsPath \ "horizontalAccuracy").read[Int] and
      (JsPath \ "verticalAccuracy").read[Int]
    )((deviceName, sudid, latitude, longitude, altitude, horizontalAccuracy, verticalAccuracy) => Pulse(deviceName, sudid, Location(latitude, longitude, altitude, horizontalAccuracy, verticalAccuracy)))

  implicit val nestedPulseReads: Reads[Pulse] = (
    (JsPath \ "deviceName").read[String] and
      (JsPath \ "sudid").read[String] and
      (JsPath \ "location").read[Location]
    )(Pulse.apply _)


  def savePulse = DBAction(BodyParsers.parse.json) {
    implicit request =>
      request.body.validate[Pulse](normalPulseReads).fold(
        errorsNormal => {
          request.body.validate[Pulse](nestedPulseReads).fold(
            errorsNested => {
              BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toFlatJson(errorsNested)))
            },
            pulseNested => {
              Pulses.insert(pulseNested)
              Ok(Json.toJson(pulseNested))
            })
        },
        pulseNormal => {
          Pulses.insert(pulseNormal)
          Ok(Json.toJson(pulseNormal))
        }
      )
  }

}
