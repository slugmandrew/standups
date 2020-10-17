name := """scala-play-react-seed"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala).settings(
  watchSources ++= (baseDirectory.value / "public/ui" ** "*").get
)

resolvers += Resolver.sonatypeRepo("snapshots")

scalaVersion := "2.12.8"

// core
libraryDependencies ++= Seq(
  evolutions,
//  jdbc,
  ehcache,
  ws,
  specs2 % Test,
  guice)

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.2" % Test

// database
libraryDependencies += "com.typesafe.play" %% "play-slick" % "4.0.2" withSources()
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "4.0.2" withSources()
//libraryDependencies += "com.h2database" % "h2" % "1.4.199"
libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1201-jdbc41" withSources()