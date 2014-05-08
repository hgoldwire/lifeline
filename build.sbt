import play.Project._

name := "lifeline"

version := "1.0"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "0.6.0.1" withJavadoc() withSources(),
  "mysql" % "mysql-connector-java" % "5.1.30",
  "org.mongodb" %% "casbah" % "2.7.1",
  "joda-time" % "joda-time" % "2.3" withJavadoc() withSources(),
  "org.joda" % "joda-convert" % "1.5" withJavadoc() withSources(),
  "com.github.tototoshi" %% "slick-joda-mapper" % "1.1.0" withJavadoc() withSources()
)

playScalaSettings

initialCommands in console := """
  import models._
  import play.api.libs.json._
  import play.api.libs.functional.syntax._
  import play.api.db.slick.Config.driver.simple._
"""