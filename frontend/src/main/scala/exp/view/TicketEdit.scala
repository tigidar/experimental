package exp.view

import exp.ext.*
import exp.api.Api
import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.{*, given}
import exp.events.{Events, DataEvent}
import exp.domain.Ticket
import exp.domain.info

def InputField(
  placeholderText: Option[String],
  text: Option[String]
) =
  input(
    placeholder := placeholderText.?~,
    text.?~,
    margin := 15.px,
    border := 2.px
  )

object TicketEdit:

  def view(ticket: Ticket) =
    List(
      "id" -> ticket.id,
      "title" -> ticket.justification.info.title,
      "description" -> ticket.justification.info.description,
    ).map: (label, data) =>
      div(
        span(margin := "10px", label),
        InputField(None, None)
      )

  def render(ticket: Ticket) =
    div(
      h1("Edit"),
      view(ticket)
    )

