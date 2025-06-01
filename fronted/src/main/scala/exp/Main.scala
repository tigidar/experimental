package exp

import kyo.*
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

object ExperimentalApp extends KyoApp {

  val test: Unit < 10 =
    IO(println(42))

  def app2 =  {
        for
            _            <- Console.printLine(s"Main args: $args")
            currentTime  <- Clock.now
            _            <- Console.printLine(s"Current time is: $currentTime")
            randomNumber <- Random.nextInt(100)
            _            <- Console.printLine(s"Generated random number: $randomNumber")
        yield
        // The produced value can be of any type and is
        // automatically printed to the console.
        "example"
    }

  run {
    "example"
  }
//@main

  /*run {
    /*
    renderOnDomContentLoaded(
      appContainer,
      div(
        width := "100%",
        height := "100%",
        Menu.view,
        Page.view
      )
    )*/
    // test.runSync()
    "example"
  }
  */
}
