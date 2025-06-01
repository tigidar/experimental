package exp

import kyo.*
import exp.backend.{Api, DeveloperApi}
import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

lazy val appContainer =
  dom.document.getElementById(
    "app"
  )

enum PageEvent:
  case Home
  case Todo

lazy val eventBus = new EventBus[PageEvent]()

object Menu:

  def view: HtmlElement =
    div(
      width := "100%",
      height := "50px",
      button(
        "Home",
        onClick --> { (e: dom.MouseEvent) => eventBus.emit(PageEvent.Home) }
      ),
      button(
        "Todo",
        onClick --> { _ => eventBus.emit(PageEvent.Todo) }
      )
    )

object Page:
  def view: HtmlElement =
    div(
      width := "100%",
      height := "100%",
      child <-- eventBus.events.toSignal(PageEvent.Home).map {
        case PageEvent.Home => h1("Welcome to the Home Page!")
        case PageEvent.Todo => h1("Todo List")
      }
    )

def renderDom(): Unit < (Env[Api] & IO) =
  for
    api <- Env.get[Api]
    _ <- IO(
      render(
        appContainer,
        div(
          width := "100%",
          height := "100%",
          Menu.view,
          Page.view
        )
      )
    )
  yield ()

object ExperimentalApp extends KyoApp {

  lazy val startApp: Unit < IO = 
    Memo.run(Env.runLayer(DeveloperApi.backendApiLayer)(renderDom()))

  run {
    for  
      _ <- startApp 
    yield ()
  }

}
