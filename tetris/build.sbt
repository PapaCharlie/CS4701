name := "tetris"

version := "0.1"

scalaVersion := "2.11.6"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

initialize := {
  val required = "1.8"
  val current = sys.props("java.specification.version")
  assert(current == required, s"Unsupported JDK: java.specification.version $current != $required")
  assert(!System.getenv("DATABASE_URL").isEmpty, "DATABASE_URL environment variable must be set!")
}

lazy val tetris =
  project.in(file("."))
    .settings(Common.settings: _*)

mainClass in(Compile, run) := Some("tetris.Main")