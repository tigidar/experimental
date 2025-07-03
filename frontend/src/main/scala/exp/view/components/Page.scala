package exp.view.components

import org.scalajs.dom
import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.{*, given}

import exp.view.{TicketList, TicketEdit}
import exp.events.{PageEvent, Events}
import exp.api.Api

object Page:

  def view(): HtmlElement =
    div(
      width := "100%",
      height := "100%",
      child <-- Events.pageRx.map {
        case PageEvent.Home => h1("Welcome to the Home Page!")
        case PageEvent.TicketList => TicketList.render()
        case PageEvent.TicketEdit(ticket) => TicketEdit.render(ticket)
      }
    )

