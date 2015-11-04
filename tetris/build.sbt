name := "tetris"

version := "0.1"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

lazy val tetris =
  project.in(file("."))
    .settings(Common.settings: _*)

mainClass in(Compile, run) := Some("tetris.Main")

mainClass in assembly := Some("tetris.Main")