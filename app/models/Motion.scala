package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

// JSON library

// Custom validation helpers

// Combinator syntax


/**
 * Created by henry.goldwire on 5/5/14.
 */
case class Motion(walking: Boolean = false, running: Boolean = false)

object Motion {

  def toBoolean(value: Boolean): Reads[JsValue] = {
    Reads.of[List[String]].map(keys => Json.toJson(keys.map(_ -> value).toMap))
  }

  val transformation =
    (__ \ 'include).json.update(toBoolean(true)) andThen
      (__ \ 'exclude).json.update(toBoolean(false))

  val example = Json.parse( """{
  "include": ["field1", "field2", "field3"],
  "exclude": ["field4", "field5", "field6"]
}""")

  val m: JsValue = Json.parse( """{"motion": ["walking", "running"]}""")

  val tb = (__ \ 'motion).json.update(toBoolean(true))



  val r = m.validate(tb)

}
