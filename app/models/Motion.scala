package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

// JSON library

// Custom validation helpers

// Combinator syntax


/**
 * Created by henry.goldwire on 5/5/14.
 */
case class Motion(walking: Boolean = false, running: Boolean = false, driving: Boolean = false)

object Motion {

  def toBoolean(value: Boolean): Reads[JsValue] = {
    Reads.of[List[String]].map(keys => Json.toJson(keys.map(_ -> value).toMap))
  }

  val transformer = (__ \ 'motion).json.update(toBoolean(true))

  val beforeJson: JsValue = Json.parse( """{"motion": ["walking", "running"]}""")
  //  val transformedJson: JsValue = Json.parse("""{"walking":true,"running":true}""")

  val transformed = beforeJson.transform(transformer)

  implicit val motionWrites: Writes[Motion] = (
    (JsPath \ 'walking).write[Boolean] and
      (JsPath \ 'running).write[Boolean] and
      (JsPath \ 'driving).write[Boolean]
    )(unlift(Motion.unapply))


  implicit val transformedMotionReads: Reads[Motion] = (
    (JsPath \ 'walking).read[Boolean] and
      (JsPath \ 'running).read[Boolean] and
      ((JsPath \ 'driving).read[Boolean] orElse (() => JsSuccess(false)))
    )(Motion.apply _)
}
