import sbt._
import java.util.jar.Attributes.Name._

abstract class ScalazDefaults(info: ProjectInfo) extends DefaultProject(info) with OverridableVersion
        with AutoCompilerPlugins {
  val scalaToolsSnapshots = "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/"

  private val encodingUtf8 = List("-encoding", "UTF-8")

  override def compileOptions =
    encodingUtf8.map(CompileOption(_)) :::
            target(Target.Java1_5) :: Unchecked :: super.compileOptions.toList

  override def packageOptions = ManifestAttributes((IMPLEMENTATION_TITLE, "Scalaz"), (IMPLEMENTATION_URL, "http://code.google.com/p/scalaz"), (IMPLEMENTATION_VENDOR, "The Scalaz Project"), (SEALED, "true")) :: Nil

  override def documentOptions = encodingUtf8.map(SimpleDocOption(_)): List[ScaladocOption]

  override def managedStyle = ManagedStyle.Maven

  override def packageSrcJar = defaultJarPath("-sources.jar")

  override def outputPattern = "[conf]/[type]/[artifact](-[revision])(-[classifier]).[ext]"

  lazy val sourceArtifact = Artifact(artifactID, "src", "jar", Some("sources"), Nil, None)

  override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageSrc)

  // Workaround for problem described here: http://groups.google.com/group/simple-build-tool/browse_thread/thread/7575ea3c074ee8aa/373a91c25393085c?#373a91c25393085c
  override def deliverScalaDependencies = Nil

    override def consoleInit =
"""
import org.specs2.internal.scalaz._
import Scalaz._

"""
}

final class ScalazProject(info: ProjectInfo) extends ParentProject(info) with OverridableVersion {
  // Sub-projects
  lazy val core = project("core", "scalaz-core", new Core(_))
  lazy val allModules = Seq(core)

  /** Publishing */
  override def managedStyle = ManagedStyle.Maven
  override def defaultPublishRepository = {
    val nexusDirect = "http://nexus-direct.scala-tools.org/content/repositories/"
    if (version.toString.endsWith("SNAPSHOT")) 
	  Some("scala-tools snapshots" at nexusDirect + "snapshots/")
	else
	  Some("scala-tools releases" at nexusDirect + "releases/")
  }
  Credentials(Path.userHome / ".ivy2" / ".credentials", log)

  private def noAction = task {None}

  override def deliverLocalAction = noAction

  override def publishLocalAction = noAction

  override def publishAction = task {None}

  val parentPath = path _

  class Core(info: ProjectInfo) extends ScalazDefaults(info) with Boilerplate {
    override def mainSourceRoots = super.mainSourceRoots +++ srcManagedScala##
    override def compileAction = super.compileAction dependsOn(generateTupleW)

    override def documentOptions = documentTitle("Scalaz Core") :: super.documentOptions
  }
}
