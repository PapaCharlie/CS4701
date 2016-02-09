name := "tetris"

version := "1.0"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

// scalaVersion := "2.11.7"

lazy val tetris =
  project.in(file("."))
    .settings(Common.settings: _*)

mainClass in(Compile, run) := Some("tetris.Main")

mainClass in assembly := Some("tetris.Main")

publishTo := Some(Resolver.file("file", new File("maven")))

isSnapshot := true
