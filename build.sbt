ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"


libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"

val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)


lazy val root = (project in file("."))
  .settings(
    name := "Aurinkokuntasimulaattori"
  )
