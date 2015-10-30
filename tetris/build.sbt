name := "tetris"

version := "0.1"

scalaVersion := "2.11.6"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature")


lazy val tetris =
  project.in(file("."))
    .settings(Common.settings: _*)

mainClass in(Compile, run) := Some("tetris.Main")
