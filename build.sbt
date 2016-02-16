import scalariform.formatter.preferences._

name := "play-silhouette-slick-seed"

version := "3.0.1"

scalaVersion := "2.11.7"

resolvers := ("Atlassian Releases" at "https://maven.atlassian.com/public/") +: resolvers.value

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "com.mohiva" %% "play-silhouette" % "3.0.2",
  "org.webjars" %% "webjars-play" % "2.4.0-1",
  "net.codingwell" %% "scala-guice" % "4.0.0",
  "com.adrianhurt" %% "play-bootstrap3" % "0.4.4-P24",
  "com.mohiva" %% "play-silhouette-testkit" % "3.0.2" % "test",
  specs2 % Test,
  "com.typesafe.play" %% "play-slick" % "1.0.1",
  "com.typesafe.play" %% "play-slick-evolutions" % "1.0.1",
  "com.h2database" % "h2" % "1.4.188",
  "net.ceedubs" %% "ficus" % "1.1.2",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  cache,
  evolutions,
  filters
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)

routesGenerator := InjectedRoutesGenerator