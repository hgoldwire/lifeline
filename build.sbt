import play.Project._

name := "lifeline"

version := "1.0"

libraryDependencies += "com.typesafe.play" %% "play-slick" % "0.6.0.1"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.30"

playScalaSettings

initialCommands in console := """
  import models._
  import play.api.libs.json._
  import play.api.libs.functional.syntax._
  import play.api.db.slick.Config.driver.simple._
"""