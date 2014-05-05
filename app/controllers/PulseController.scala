package controllers

import models._
import play.api._
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.Play.current
import play.api.mvc.BodyParsers._
import play.api.libs.json.{JsError, Json}
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

  //  def listPulses() = Action {
  //    val json = Json.toJson(Pulses.list)
  //    Ok(json)
  //  }


  def savePulse = DBAction(BodyParsers.parse.json) {
    implicit request =>
        val pulseResult = request.body.validate[Pulse]
        pulseResult.fold(
          errors => {
            BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))
          },
          pulse => {
            Pulses.insert(pulse)
            Ok(Json.toJson(pulse))
          }
        )
  }

}
