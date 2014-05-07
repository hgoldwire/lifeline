package controllers

import models._
import models.Pulse.pulseReads
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


  def savePulse = DBAction(BodyParsers.parse.json) {
    implicit request =>
      val json = request.body
      val transformedMotion = json.transform(models.Motion.transformer)
      val pulseResult = transformedMotion.flatMap(json => json.validate[Pulse](pulseReads))
      pulseResult.fold(invalid => {
        println(request.body)
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(invalid)))
      }, valid => {
        Pulses.insert(valid)
        Ok(Json.toJson(valid))
      })
  }
}
