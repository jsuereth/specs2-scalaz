/** Project */
name := "specs2-scalaz-core"

version := "6.0.1"

organization := "org.specs2"

scalaVersion := "2.9.1"

/** Shell */
shellPrompt := { state => System.getProperty("user.name") + "> " }

shellPrompt in ThisBuild := { state => Project.extract(state).currentRef.project + "> " }

/** Dependencies */
resolvers ++= Seq("snapshots-repo" at "http://scala-tools.org/repo-snapshots", 
                  "Local Maven Repository" at "file://c:/Documents and Settings/Eric/.m2/repository")

/** Compilation */
javacOptions ++= Seq("-Xmx1812m", "-Xms512m", "-Xss4m")

javaOptions += "-Xmx2G"

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

maxErrors := 20 

pollInterval := 1000

/** Console */
initialCommands in console := "import org.specs2._"

// Packaging

// disable publishing the test API jar
publishArtifact in (Compile, packageDoc) := false

/** Publishing */
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

publishTo <<= (version) { version: String =>
  val nexus = "http://nexus-direct.scala-tools.org/content/repositories/"
  if (version.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus+"snapshots/") 
  else                                   Some("releases" at nexus+"releases/")
}
