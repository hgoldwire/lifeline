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

  def listPulses = DBAction {
    implicit rs =>
      Ok(toJson(Pulses.list))
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

      val chunkSize = 10000;
      var chunks = scala.collection.mutable.ListBuffer[Pulse]();

      def chunkedInsert(pulse: Pulse) {
        chunks.append(pulse)
        if (chunks.size % 100 == 0) {
          print("chunks: " + chunks.size)
        }
        if (chunks.size >= chunkSize) {
          Pulses.insertAll(chunks: _*)
          chunks = scala.collection.mutable.ListBuffer[Pulse]()
        }
      }

      val allDocs = coll.find()
      for (doc <- allDocs) {
        val jsonDoc = doc.toString
        try {
          val parsed = Json.parse(jsonDoc)
          parsed.validate[Pulse].fold(error => {
            println("can't insert " + jsonDoc)
            println(error)
            println("------------------------------------------------------------")
          }, pulse => {
            chunkedInsert(pulse)
          })
        } catch {
          case e: Exception => println("super gnarly exception: " + e + " for " + e )
        }
      }
      Ok("hi")
  }
}

//object ImportMongo {
//
//  val Pulses = TableQuery[PulsesTable] //see a way to architect your app in the computers-database-slick sample
//
//  def apply = {
//    import com.mongodb.casbah.Imports._
//    val mongoClient = MongoClient("localhost", 27017)
//    val db = mongoClient("lifeline")
//    val coll = db("pulse")
//
//    val allDocs = coll.find()
//    DB.withSession( implicit session =>
//      for (doc <- allDocs) {
//        val jsonDoc = doc.toString
//        Json.parse(jsonDoc).validate[Pulse].fold(error => {
//          println("can't insert " + jsonDoc)
//          println(error)
//          println("------------------------------------------------------------")
//        }, pulse => {
//          Pulses.insert(pulse)
//        })
//      }
//    )
//  }
//}