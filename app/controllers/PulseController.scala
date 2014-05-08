package controllers

import models._
import models.Pulse.pulseReads
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.json.Json._


/**
 * Created by henry.goldwire on 5/5/14.
 */
object PulseController extends Controller {

  val Pulses = TableQuery[PulsesTable] //see a way to architect your app in the computers-database-slick sample

  def get(deviceName: Option[String], interval: Option[Int]) = DBAction {
    implicit rs =>

//      val pulses = deviceName match {
//        case Some(name) => for {
//          p <- Pulses if p.deviceName === deviceName
//        } yield (p)
//        case None => Pulses
//      }

//      val bucketized = interval match {
//        case Some(i) => IntervalBucketizer.bucketize(i, pulses.list())
//        case None => pulses.list
//      }
//      Ok(toJson(bucketized))
      Ok("Hi")
  }

  def savePulse = DBAction(BodyParsers.parse.json) {
    implicit request =>
      val json = request.body
      json.validate[Pulse].fold(invalid => {
        println(request.body)
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(invalid)))
      }, valid => {
        Pulses.insert(valid)
        Ok(Json.toJson(valid))
      })
  }

  def importMongo = DBAction {
    implicit request =>
      import com.mongodb.casbah.Imports._

      val mongoClient = MongoClient("localhost", 27017)
      val db = mongoClient("lifeline")
      val coll = db("pulse")

      val allDocs = coll.find()
      for (doc <- allDocs) {
        val jsonDoc = doc.toString
        try {
          val parsed = Json.parse(jsonDoc)
          parsed.validate[Pulse].fold(error => {
          }, pulse => {
            Pulses.insert(pulse)
          })
        } catch {
          case e: Exception => //println("super gnarly exception: " + e + " for " + jsonDoc)
        }
      }
      Ok("hi")
  }
}
