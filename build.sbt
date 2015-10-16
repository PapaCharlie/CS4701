version := "0.1"

scalaVersion := "2.11.6"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

lazy val tetris =
  project.in(file("tetris"))
    // .settings(Common.settings: _*)
    // .aggregate(macros).dependsOn(sql, macros)
    // .enablePlugins(SbtTwirl, SbtWeb)