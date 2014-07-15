package controllers

import models._
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.json._


/**
 * Created by henry.goldwire on 5/5/14.
 */
object DashboardController extends Controller {

  val Pulses = TableQuery[PulsesTable] //see a way to architect your app in the computers-database-slick sample

  def get(sudid: String) = DBAction {
    implicit rs =>
      val pulses = for {
        p <- Pulses if p.sudid === sudid
      } yield (p)

      Ok(views.html.dashboard("Title"))

  }
}
