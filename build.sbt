import org.scalajs.linker.interface.ModuleSplitStyle
import org.scalajs.jsenv.nodejs.*
import org.typelevel.scalacoptions.ScalacOption
import org.typelevel.scalacoptions.ScalacOptions
import org.typelevel.scalacoptions.ScalaVersion

lazy val scalaVersionDef = "3.7.1"
lazy val tapirVersion = "1.11.35"
lazy val kyoVersion = "0.19.0"
lazy val sourcecodeVersion = "0.4.2"
lazy val sttpVersion = "4.0.7"
lazy val fs2Version = "3.12.0"
lazy val scalaJavaTimeVersion = "2.6.0"
lazy val scalaJsDomVersion = "2.8.0"
lazy val scalaUriVersion = "4.1.0"
lazy val laminarVersion = "17.2.1"

val compilerOptionFailDiscard =
  "-Wconf:msg=(unused.*value|discarded.*value|pure.*statement):error"

val compilerOptions = Set(
  ScalacOptions.encoding("utf8"),
  ScalacOptions.feature,
  ScalacOptions.unchecked,
  ScalacOptions.deprecation,
  ScalacOptions.warnValueDiscard,
  ScalacOptions.warnNonUnitStatement,
  ScalacOptions.languageStrictEquality,
  ScalacOptions.release("11"),
  ScalacOptions.advancedKindProjector
)

def scalacOptionToken(proposedScalacOption: ScalacOption) =
  scalacOptionTokens(Set(proposedScalacOption))

def scalacOptionTokens(proposedScalacOptions: Set[ScalacOption]) = Def.setting {
  val version = ScalaVersion.fromString(scalaVersionDef).right.get
  ScalacOptions.tokensForVersion(version, proposedScalacOptions)
}

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

lazy val frontendLibs = Def.setting(
  Seq(
    "org.scala-js" %%% "scalajs-dom" % scalaJsDomVersion,
    "com.raquo" %%% "laminar" % laminarVersion,
    "com.indoorvivants" %%% "scala-uri" % scalaUriVersion,
    "org.scalameta" %%% "munit" % "1.1.0" % Test
  )
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
    "com.softwaremill.sttp.client4" %%% "core" % sttpVersion,
    "com.softwaremill.sttp.tapir" %%% "tapir-json-pickler" % tapirVersion
  )
)

lazy val commonSettings = Seq(
  scalaVersion := scalaVersionDef,
  scalacOptions ++= scalacOptionToken(ScalacOptions.source3).value
)

lazy val domain = crossProject(JVMPlatform, JSPlatform)
  .in(file("domain"))
  .settings(commonSettings)
  .enablePlugins(ScalaJSPlugin) // Enable the Scala.js plugin in this project
  .settings(
    libraryDependencies := supportLibsJs.value
  )

lazy val codecs = crossProject(JVMPlatform, JSPlatform)
  .in(file("codecs"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= tapirDepsJs.value
  )
  .dependsOn(domain)

lazy val endpoints = crossProject(JVMPlatform, JSPlatform)
  .in(file("endpoints"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= supportLibsJs.value ++ tapirDepsJs.value
  )
  .dependsOn(codecs)

lazy val stubBackend = crossProject(JVMPlatform, JSPlatform)
  .in(file("stub"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %%% "tapir-sttp-stub4-server" % tapirVersion
      // ("org.scala-js" %%% "scalajs-java-securerandom" % "1.0.0")
      // .cross(CrossVersion.for3Use2_13)
    ) ++ tapirDepsJs.value
  )
  .dependsOn(endpoints)

lazy val frontend = project
  .enablePlugins(ScalaJSPlugin) // Enable the Scala.js plugin in this project
  .settings(commonSettings)
  .settings(
    libraryDependencies ++=
      frontendLibs.value ++
        supportLibsJs.value ++
        kyoDepsJs.value ++
        tapirDepsJs.value,

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
  .dependsOn(endpoints.js, stubBackend.js)

lazy val database = project
  .in(file("db"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "scalasql" % "0.1.20"
    )
  )

lazy val nettyWithCats = project
  .in(file("netty"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      // if you are using cats-effect:
      "com.softwaremill.sttp.tapir" %% "tapir-netty-server-cats" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-sttp-client4" % tapirVersion,
      "org.apache.pekko" %% "pekko-http" % "1.0.1",
      "org.apache.pekko" %% "pekko-stream" % "1.0.3",
      "ch.qos.logback" % "logback-classic" % "1.5.6"
    ) ++ ioLibs
  )
  .dependsOn(
    endpoints.jvm
  )

lazy val root = project
  .in(file("."))
  .aggregate(
    domain.js,
    domain.jvm,
    codecs.js,
    codecs.jvm,
    stubBackend.js,
    stubBackend.jvm,
    endpoints.js,
    endpoints.jvm,
    frontend,
    database,
    nettyWithCats
  )
