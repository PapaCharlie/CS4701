import sbt._
import sbt.Keys._

object Common {

  val akkaV = "2.3.9"

  val dependencies = Seq(
    // "com.typesafe.akka"   %%  "akka-actor"        % akkaV,
    // "com.typesafe.akka"   %%  "akka-testkit"      % akkaV   % "test",
    "org.apache.spark"        %%  "spark-core"      % "1.5.1"   % "provided",
    "commons-io"              %   "commons-io"      % "2.4",
    "org.scala-lang.modules"  %%  "scala-pickling"  % "0.10.1",
    "org.specs2"              %%  "specs2-core"     % "2.3.11"  % "test",
    "org.scalatest"           %   "scalatest_2.11"  % "2.2.4"   % "test",
    "org.scalacheck"          %%  "scalacheck"      % "1.12.5"  % "test",
    "org.scala-saddle"        %%  "saddle-core"     % "1.3.+"

  )

  val names: Seq[Setting[_]] = Seq(
    version := "0.1",
    scalaVersion := "2.11.7"
  )

  val settings: Seq[Setting[_]] = names ++ Seq(
    libraryDependencies ++= dependencies,
    resolvers += "softprops-maven" at "https://dl.bintray.com/content/softprops/maven",
    resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run)),
    scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-optimise"),
    javaOptions ++= Seq("-d64", "-Xms512m", "-Xmx4g")
    // initialize := {
    //   val required = "1.8"
    //   val current = sys.props("java.specification.version")
    //   assert(current == required, s"Unsupported JDK: java.specification.version $current != $required")
    // }
  )
}