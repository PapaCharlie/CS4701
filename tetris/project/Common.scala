import sbt._
import sbt.Keys._

object Common {

  val akkaV = "2.3.9"

  val dependencies = Seq(
    // "com.typesafe.akka"   %%  "akka-actor"        % akkaV,
    // "com.typesafe.akka"   %%  "akka-testkit"      % akkaV   % "test",
    "org.specs2"          %%  "specs2-core"       % "2.3.11" % "test"
  )

  val names: Seq[Setting[_]] = Seq(
    // organization := "com.janesoft",
    version := "0.1",
    scalaVersion := "2.11.6"
  )

  val settings: Seq[Setting[_]] = names ++ Seq(
    libraryDependencies ++= dependencies,
    resolvers += "softprops-maven" at "https://dl.bintray.com/content/softprops/maven",
    resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")
  )
}
