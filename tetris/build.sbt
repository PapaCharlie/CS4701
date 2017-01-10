name := "tetris"

lazy val tetris =
  project.in(file("."))
    .settings(Common.settings: _*)

mainClass in(Compile, run) := Some("tetris.Main")

mainClass in assembly := Some("tetris.Main")

publishTo := Some(Resolver.file("file", new File("maven")))

isSnapshot := true
