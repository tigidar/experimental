package exp.view

import exp.ext.*
import exp.domain.info
import exp.api.Api
import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.{*, given}
import exp.events.{Events, DataEvent}
import exp.domain.Ticket

object TicketView:

  def view(ticket: Ticket) =
    val info = ticket.justification.info
    List(
      "id" -> ticket.id.toString,
      "title" -> info.title,
      "description" -> info.description
    ).map: (label, data) =>
      div(
        span(margin := 10.px, label),
        span(data)
      )

  def render(ticket: Ticket) =
    div(
      h1("View"),
      view(ticket)
    )

