package exp.view.components

import org.scalajs.dom
import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.{*, given}
import exp.events.{PageEvent, Events}

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
