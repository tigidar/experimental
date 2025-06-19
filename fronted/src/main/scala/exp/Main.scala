package exp

import kyo.*
import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

import exp.backend.{Api, DeveloperApi}
import exp.view.components.{Menu, Page}
import exp.events.{Events, PageEvent }

lazy val appContainer =
  dom.document.getElementById(
    "app"
  )


def renderDom(): Unit < (Env[Api] & IO) =

  def pageEventObserver(api: Api): L.Observer[PageEvent] = Observer {
    //event => exp.events.Events.page.writer.onNext(event)
    case PageEvent.Todo =>
      //TODO: Fetch todos from the API and update the state
      println("todo")
    case _ =>
  }

  for
    api <- Env.get[Api]
    _ <- IO(
      render(
        appContainer,
        div(
          width := "100%",
          height := "100%",
          Menu.view,
          Page.view(),
          Events.page.events --> pageEventObserver(api),
        )
      )
    )
  yield ()

object ExperimentalApp extends KyoApp {

  lazy val startApp: Unit < IO =
    Memo.run(Env.runLayer(DeveloperApi.backendApiLayer)(renderDom()))

  run {
    for _ <- startApp
    yield ()
  }

}
