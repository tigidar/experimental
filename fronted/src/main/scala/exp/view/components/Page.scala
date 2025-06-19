package exp.view.components

import org.scalajs.dom
import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.{*, given}

import exp.view.TodoPage
import exp.events.{PageEvent, Events}
import exp.backend.{Api, DeveloperApi}

object Page:

  val pageRx = Events.page.events.toSignal(PageEvent.Todo)

  def view(): HtmlElement =
    div(
      width := "100%",
      height := "100%",
      child <-- pageRx.map {
        case PageEvent.Home => h1("Welcome to the Home Page!")
        case PageEvent.Todo => TodoPage().render()
      }
    )
