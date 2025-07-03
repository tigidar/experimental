package exp.view.components

import org.scalajs.dom
import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.{*, given}
import exp.events.{PageEvent, Events}

object Menu:

  val menu = List(
    "Home" -> PageEvent.Home,
    "Ticket list" -> PageEvent.TicketList
  ).map {
    case (title, page) => button(
      title,
      onClick --> { (e: dom.MouseEvent) => Events.page.emit(page) }
    )
  }

  def view: HtmlElement =
    div(
      width := "100%",
      height := "50px",
      menu
    )

