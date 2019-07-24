name := """techTask"""
organization := "de.autoscout24"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.0"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test
libraryDependencies += "com.h2database" % "h2" % "1.4.192"
libraryDependencies += jdbc

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "de.autoscout24.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "de.autoscout24.binders._"
