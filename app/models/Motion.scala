package models

import play.api.libs.json._
import play.api.libs.json.JsObject
import play.api.libs.json.JsBoolean
import play.api.data.validation.ValidationError
import play.api.libs.json.JsSuccess


/**
 * Created by henry.goldwire on 5/5/14.
 */
case class Motion(walking: Boolean = false, running: Boolean = false, driving: Boolean = false)

object Motion {

  implicit object MotionReads extends Reads[Motion] {
    def reads(json: JsValue) = json.validate[Seq[String]].fold(errors => {
      JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jsarray"))))
    }, motions => {
      val isWalking = motions.contains("walking")
      val isRunning = motions.contains("running")
      val isDriving = motions.contains("driving")
      JsSuccess(Motion(isWalking, isRunning, isDriving))
    })
  }

  implicit object MotionWrites extends Writes[Motion] {
    def writes(motion: Motion) = {
      JsObject(Seq(("walking", JsBoolean(motion.walking)), ("driving", JsBoolean(motion.driving)), ("running", JsBoolean(motion.running))))
    }
  }

}