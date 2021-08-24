name := "rock-the-jvm-scala-advanced"
version := "0.1"
scalaVersion := "2.13.6"

libraryDependencies ++= List(
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.3",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value
)