enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)

name := "UpNorth"

version := "0.1.0"

organization := "Your organization"

scalaVersion := "2.12.2"

requiresDOM in Test := true

libraryDependencies ++= Seq(
  "io.github.outwatch" %%% "outwatch" % "0.10.1",
  "org.typelevel" %%% "cats" % "0.9.0",
  "org.scalatest" %%% "scalatest" % "3.0.1" % Test
)
