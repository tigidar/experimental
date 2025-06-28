package exp.view

import exp.ext.*
import exp.api.Api
import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.{*, given}
import exp.events.{Events, DataEvent}
import exp.model.Ticket

object TicketView:

  def view(ticket: Ticket) =
    List(
      "id" -> ticket.id,
      "title" -> ticket.title,
      "description" -> ticket.description
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

