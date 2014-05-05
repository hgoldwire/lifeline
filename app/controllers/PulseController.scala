package controllers

import play.api.mvc._
import play.api.libs.json._

//import play.api.libs.functional.syntax._

import models.Pulse


/**
 * Created by henry.goldwire on 5/5/14.
 */
object PulseController extends Controller {

  def listPulses() = Action {
    val json = Json.toJson(Pulse.list)
    Ok(json)
  }


  def savePulse = Action(BodyParsers.parse.json) {
    request =>
      val pulseResult = request.body.validate[Pulse]
      pulseResult.fold(
        errors => {
          BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))
        },
        pulse => {
          Pulse.save(pulse)
          Ok(Json.obj("status" -> "OK", "message" -> ("Place '" + pulse.deviceName + "' saved.")))
        }
      )
  }

}
