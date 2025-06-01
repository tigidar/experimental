import org.scalajs.linker.interface.ModuleSplitStyle
import ujson.Value

lazy val externalDepsJs = settingKey[Seq[ModuleID]]("deps.json → ModuleID list")

lazy val fronted = project
  .enablePlugins(ScalaJSPlugin) // Enable the Scala.js plugin in this project
  .settings(
    externalDepsJs := {
      val raw = IO.read(baseDirectory.value / "deps.json")
      val arr = ujson.read(raw).arr
      arr.map { v =>
        val org = v("group").str
        val art = v("artifact").str
        val ver = v("version").str
        //val art0 = s"${art}_${scalaVersion.value.takeWhile(_ != '.')}"

        val dep0 = org %%% art % ver
        v.obj.get("scope").fold(dep0)(s => dep0 % s.str)
      }.toSeq
    },
    scalaVersion := "3.7.0",

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
    },

    /* Depend on the scalajs-dom library.
     * It provides static types for the browser DOM APIs.
     */
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "2.8.0",

      // Depend on Laminar
      "com.raquo" %%% "laminar" % "17.2.1",

      // "com.indoorvivants" %% "scala-uri" % "4.1.0"
      "com.indoorvivants" %%% "scala-uri" % "4.1.0",
      "com.softwaremill.sttp.client4" %%% "core" % "4.0.0",
      // Testing framework
      "org.scalameta" %%% "munit" % "1.1.0" % Test
    ),
    libraryDependencies ++= externalDepsJs.value
  )

lazy val root = project
  .in(file("."))
  .aggregate(fronted)
