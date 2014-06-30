package models

import play.api.libs.json._
import play.api.libs.json.JsObject
import play.api.libs.json.JsBoolean
import play.api.libs.functional.syntax._


/**
 * Created by henry.goldwire on 5/5/14.
 */
case class Motion(speed: Int, walking: Boolean = false, running: Boolean = false, driving: Boolean = false)

object Motion {

  implicit val motionReads: Reads[Motion] = (
    (JsPath \ "speed").read[Int] and
      (JsPath \ "isWalking").read[Boolean] and
      (JsPath \ "isRunning").read[Boolean] and
      (JsPath \ "isDriving").read[Boolean]
    )(Motion.apply _)

  implicit val motionWrites: Writes[Motion] = (
    (JsPath \ "speed").write[Int] and
      (JsPath \ "isWalking").write[Boolean] and
      (JsPath \ "isRunning").write[Boolean] and
      (JsPath \ "isDriving").write[Boolean]
    )(unlift(Motion.unapply))

  //  implicit object MotionWrites extends Writes[Motion] {
  //    def writes(motion: Motion) = {
  //      val speed = JsObject(Seq(("speed", JsNumber(motion.speed))))
  //      val movement = JsObject(Seq(("walking", JsBoolean(motion.walking)), ("driving", JsBoolean(motion.driving)), ("running", JsBoolean(motion.running))))
  //    }
  //  }

}