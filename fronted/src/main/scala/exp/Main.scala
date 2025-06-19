package exp

import exp.view.TodoPage
import exp.events.{PageEvent, Events}
import kyo.*
import exp.backend.{Api, DeveloperApi}
import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

lazy val appContainer =
  dom.document.getElementById(
    "app"
  )

object Menu:

  def view: HtmlElement =
    div(
      width := "100%",
      height := "50px",
      button(
        "Home",
        onClick --> { (e: dom.MouseEvent) => Events.page.emit(PageEvent.Home) }
      ),
      button(
        "Todo",
        onClick --> { _ => Events.page.emit(PageEvent.Todo) }
      )
    )

object Page:
  def view(api: Api): HtmlElement =
    div(
      width := "100%",
      height := "100%",
      child <-- Events.page.events.toSignal(PageEvent.Todo).map {
        case PageEvent.Home => h1("Welcome to the Home Page!")
        case PageEvent.Todo => TodoPage(api).render()
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
          Page.view(api)
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
