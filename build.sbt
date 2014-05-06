import play.Project._

name := "lifeline"

version := "1.0"

libraryDependencies += "com.typesafe.play" %% "play-slick" % "0.6.0.1"

playScalaSettings

initialCommands in console := """
  import play.api.libs.json._
  import play.api.libs.functional.syntax._
  import play.api.db.slick.Config.driver.simple._
"""