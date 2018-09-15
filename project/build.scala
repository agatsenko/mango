import sbt._
import sbt.Keys._

object build {
  object project {
    val org = "io.mango"
    val ver = "0.2.0"
  }

  object ver {
    val java = "1.8"
    val crossScala = Seq("2.11.12", "2.12.6")

    val slf4j = "1.7.+"
    val logback = "1.2.+"

    val hikariCp = "2.7.+"
    val h2database = "1.4.+"

    val scalatest = "3.0.5"
    val scalamock = "4.1.+"
  }

  object depends {
    val scalaLib = "org.scala-lang" % "scala-library"
    val scalaReflect = "org.scala-lang" % "scala-reflect"

    val slf4jApi = "org.slf4j" % "slf4j-api" % ver.slf4j
    val logbackClassic = "ch.qos.logback" % "logback-classic" % ver.logback

    val hikariCp = "com.zaxxer" % "HikariCP" % ver.hikariCp
    val h2database = "com.h2database" % "h2" % ver.h2database

    val scalatest = "org.scalatest" %% "scalatest" % ver.scalatest
    val scalamock = "org.scalamock" %% "scalamock" % ver.scalamock
  }

  val commonSettings = Seq(
    organization := project.org,
    version := project.ver,

    crossScalaVersions := ver.crossScala,

    // some info about scala compile options see in
    // http://blog.threatstack.com/useful-scalac-options-for-better-scala-development-part-1
    scalacOptions ++= Seq(
      // JVM target
      s"-target:jvm-${ver.java}",
      // Emit warning and location for usages of deprecated APIs.
      "-deprecation",
      // Enable detailed unchecked (erasure) warnings
      // Non variable type-arguments in type patterns are unchecked since they are eliminated by erasure
      "-unchecked",
      // Emit warning and location for usages of features that should be imported explicitly.
      "-feature",
      // Enable experimental extensions.
      "-Xexperimental",
      // Wrap field accessors to throw an exception on uninitialized access.
      "-Xcheckinit",
      // Enable recommended additional warnings.
      "-Xlint:_",
      // Fail the compilation if there are any warnings.
      //, "-Xfatal-warnings"
      // Warn when local and private vals, vars, defs, and types are unused.
      "-Ywarn-unused",
      // Warn when numerics are widened.
      //"-Ywarn-numeric-widen",
    ),
    compileOrder := CompileOrder.Mixed,

    excludeFilter in unmanagedSources := HiddenFileFilter || ".keepdir",
    excludeFilter in unmanagedResources := HiddenFileFilter || ".keepdir",

    publishM2Configuration := publishM2Configuration.value.withOverwrite(true),
  )

  val scalaCommonSettings = commonSettings ++ Seq(
    libraryDependencies ++= Seq(
      depends.scalaLib % scalaVersion.value,

      depends.slf4jApi % Test,
      depends.logbackClassic % Test,
      depends.scalatest % Test,
    ),
  )
}
