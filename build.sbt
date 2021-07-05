name := "SimpleScalaBackend"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.0.0"
libraryDependencies += "dev.zio" %% "zio" % "1.0.9"

//http4s
libraryDependencies += "org.http4s" %% "http4s-blaze-server" % "1.0-234-d1a2b53"
libraryDependencies += "org.http4s" %% "http4s-circe" % "1.0-234-d1a2b53"
libraryDependencies += "org.http4s" %% "http4s-dsl" % "1.0-234-d1a2b53"

//jdbc
libraryDependencies += "org.tpolecat" %% "doobie-core" % "0.13.4"
libraryDependencies += "org.tpolecat" %% "doobie-h2" % "0.13.4"

//cats
libraryDependencies += "dev.zio" %% "zio-interop-cats" % "2.5.1.0"

libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.16.0"

libraryDependencies += "io.circe" %% "circe-generic" % "0.14.1"
libraryDependencies += "io.circe" %% "circe-generic-extras" % "0.14.1"

libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.31"