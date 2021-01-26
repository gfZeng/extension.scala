name := "extension.scala"

version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies += "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.1"
libraryDependencies += "org.json4s"             %% "json4s-native"      % "3.7.0-M7"
libraryDependencies += "com.squareup.okhttp3"    % "okhttp"             % "4.9.0"
libraryDependencies += "org.log4s"              %% "log4s"              % "1.9.0"

// legacy
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.2"
