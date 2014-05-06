package models

import play.api.libs.json.{Reads, JsPath, Writes}
import play.api.libs.functional.syntax._

// JSON library

// Custom validation helpers

// Combinator syntax


/**
 * Created by henry.goldwire on 5/5/14.
 */
case class Motion(isWalking: Boolean, isRunning: Boolean)

object Motion {

  implicit val motionWrites: Writes[Motion] = (
    (JsPath \ )
    )(unlift(Location.unapply))

  implicit val motionReads: Reads[Motion] = (
    (JsPath \ 'motion \)
    )(Location.apply _)
}
