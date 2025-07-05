import org.scalajs.linker.interface.ModuleSplitStyle
import org.scalajs.jsenv.nodejs.*

lazy val tapirVersion = "1.11.35"
lazy val kyoVersion = "1.11.35"
lazy val sourcecodeVersion = "0.4.2"
lazy val sttpVersion = "4.0.7"
lazy val fs2Version = "3.12.0"
lazy val scalaJavaTimeVersion = "2.6.0"
lazy val scalaJsDomVersion = "2.8.0"
lazy val laminarVersion = "17.2.1"

def ^(deps: ModuleID*) =
  deps.toSeq

lazy val tapirDeps = ^(
  "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-json-pickler" % tapirVersion
)

lazy val kyoDeps = ^(
  "io.getkyo" %% "kyo-core" % kyoVersion,
  "io.getkyo" %% "kyo-prelude" % kyoVersion,
  "io.getkyo" %% "kyo-direct" % kyoVersion,
  "io.getkyo" %% "kyo-combinators" % kyoVersion,
  "io.getkyo" %% "kyo-sttp" % kyoVersion,
  "io.getkyo" %% "kyo-data" % kyoVersion
)

lazy val supportLibs = ^(
  "com.lihaoyi" %% "sourcecode" % sourcecodeVersion,
  "io.github.cquiroz" %% "scala-java-time" % scalaJavaTimeVersion
)

lazy val frontendLibs = ^(
  "org.scala-js" %% "scalajs-dom" % scalaJsDomVersion,
  "com.raquo" %% "laminar" % laminarVersion,
  "com.indoorvivants" %% "scala-uri" % "4.1.0"
)

lazy val ioLibs = ^(
  "com.softwaremill.sttp.client4" %% "core" % sttpVersion,
  "co.fs2" %% "fs2-core" % fs2Version
)

lazy val supportLibsJs = Def.setting(
  Seq(
    "com.lihaoyi" %%% "sourcecode" % sourcecodeVersion,
    "io.github.cquiroz" %%% "scala-java-time" % scalaJavaTimeVersion
  )
)

lazy val kyoDepsJs = Def.setting(
  Seq(
    "io.getkyo" %%% "kyo-core" % kyoVersion,
    "io.getkyo" %%% "kyo-prelude" % kyoVersion,
    "io.getkyo" %%% "kyo-direct" % kyoVersion,
    "io.getkyo" %%% "kyo-combinators" % kyoVersion,
    "io.getkyo" %%% "kyo-sttp" % kyoVersion,
    "io.getkyo" %%% "kyo-data" % kyoVersion
  )
)

lazy val tapirDepsJs = Def.setting(
  Seq(
    "com.softwaremill.sttp.tapir" %%% "tapir-core" % tapirVersion,
    "com.softwaremill.sttp.tapir" %%% "tapir-json-pickler" % tapirVersion
  )
)

lazy val commonSettings = Seq(
  scalaVersion := "3.7.1"
)

lazy val frontend = project
  .enablePlugins(ScalaJSPlugin) // Enable the Scala.js plugin in this project
  .settings(commonSettings)
  .settings(
    libraryDependencies ++=
      frontendLibs ++
        supportLibsJs.value ++
        Seq(
          // Testing framework
          "org.scalameta" %%% "munit" % "1.1.0" % Test,
          "com.softwaremill.sttp.tapir" %%% "tapir-sttp-stub4-server" % tapirVersion
        ),

    // Tell Scala.js that this is an application with a main method
    scalaJSUseMainModuleInitializer := true,

    /* Configure Scala.js to emit modules in the optimal way to
     * connect to Vite's incremental reload.
     * - emit ECMAScript modules
     * - emit as many small modules as possible for classes in the "livechart" package
     * - emit as few (large) modules as possible for all other classes
     *   (in particular, for the standard library)
     */
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withModuleSplitStyle(
          ModuleSplitStyle.SmallModulesFor(List("presentation"))
        )
    }
  )
  .dependsOn(model.js)

lazy val model = crossProject(JVMPlatform, JSPlatform)
  .in(file("model"))
  .enablePlugins(ScalaJSPlugin) // Enable the Scala.js plugin in this project
  .settings(
    scalaVersion := "3.7.1",
    libraryDependencies := supportLibsJs.value
  )

lazy val endpoints = crossProject(JVMPlatform, JSPlatform)
  .in(file("endpoints"))
  .settings(
    scalaVersion := "3.7.1",
    libraryDependencies ++= supportLibsJs.value ++ tapirDepsJs.value
  )
  .dependsOn(model)

lazy val allCrossProjects: Seq[sbt.Project] = 
  Seq(model, endpoints).foldLeft(List.empty[sbt.Project])((acc,p) => acc ++ Seq(p.jvm, p.js)).toSeq

lazy val root = project
  .in(file("."))
  .aggregate(
    model.js, model.jvm, endpoints.js, endpoints.jvm, frontend
  )

