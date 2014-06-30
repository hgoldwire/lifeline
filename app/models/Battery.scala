package models

import play.api.libs.json._
import play.api.libs.functional.syntax._


sealed trait BatteryState
case class Charging() extends BatteryState
case class Full() extends BatteryState
case class Unplugged() extends BatteryState
case class Unknown() extends BatteryState

case class BatteryLevel(level: Int) extends AnyVal

case class Battery(state: BatteryState, level: BatteryLevel)

object BatteryState {
  def apply(state: String) = {
    state match {
      case "charging" => Charging()
      case "full" => Full()
      case "unplugged" => Unplugged()
      case _ => Unknown()
    }
  }
}

object Battery {

  implicit object BatteryLevelWrites extends Writes[BatteryLevel] {
    def writes(bl: BatteryLevel) = {
      JsNumber(bl.level)
    }
  }

  implicit object BatterLevelReads extends Reads[BatteryLevel] {
    def reads(json: JsValue) = {
      json.validate[Int] match {
        case JsSuccess(v, path) => JsSuccess(BatteryLevel(v))
        case e: JsError => e
      }
    }
  }

  implicit object BatteryStateWrites extends Writes[BatteryState] {
    def writes(bs: BatteryState) = {
      bs match {
        case Charging() => JsString("charging")
        case Full() => JsString("full")
        case Unplugged() => JsString("unplugged")
        case Unknown() => JsString("unknown")
      }
    }
  }

  implicit object BatteryStateReads extends Reads[BatteryState] {
    def reads(json: JsValue) = {
      json.validate[String] match {
        case JsSuccess(s, _) => {
          s match {
            case "charging" => JsSuccess(Charging())
            case "full" => JsSuccess(Full())
            case "unplugged" => JsSuccess(Unplugged())
            case "unknown" => JsSuccess(Unknown())
          }
        }
        case _ => JsSuccess(Unknown())
      }
    }
  }

  implicit val batteryWrites: Writes[Battery] = (
    (JsPath \ "state").write[BatteryState] and
      (JsPath \ "level").write[BatteryLevel]
    )(unlift(Battery.unapply))

  implicit val batteryReads: Reads[Battery] = (
    (JsPath \ "state").read[BatteryState] and
      //      ((JsPath \ 'level).read[BatteryLevel] orElse (__ \ 'batteryLevel).read[String].map(_.toInt))
      (JsPath \ 'level).read[BatteryLevel]
    )(Battery.apply _)
}