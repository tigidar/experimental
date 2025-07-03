package exp.view

import exp.ext.*
import exp.api.Api
import org.scalajs.dom
import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.{*, given}
import exp.events.{Events, DataEvent}
import exp.events.{PageEvent, Events}
import exp.model.Ticket
import com.raquo.laminar.nodes.ReactiveHtmlElement

enum ViewState:
  self =>
  case Overview, Details

  def invert(): ViewState = self match
    case Overview => Details
    case Details  => Overview

  override def toString() = self match
    case Overview => "overview"
    case Details  => "details"

final case class TicketInnerView(
    ticket: Ticket,
    ticketSignal: L.Signal[Ticket]
) {

  val layoutCss = List(
    width := 100.%%,
    height := 50.px
  )

  def ticketEvent(ticket: Ticket): dom.MouseEvent => Unit = _ =>
    Events.page.emit(PageEvent.TicketEdit(ticket))

  val titleRx = h3( child.text <-- ticketSignal.map(_.title))

  def ticketDetails(detailsOrNot: ReactiveHtmlElement[?] | L.CommentNode) =
    div(
      layoutCss,
      titleRx,
      detailsOrNot,
      onClick --> ticketEvent(ticket)
    )

  def ticketPanel(
      viewState: Var[ViewState]
  ): HtmlElement =
    div(
      child <-- viewState.signal.map:
        case ViewState.Details =>
          ticketDetails(
            p(
              child.text <-- ticketSignal.map(_.description)
            )
          )
        case ViewState.Overview => ticketDetails(emptyNode)
    )
}

object TicketList:

  val viewState: Var[ViewState] = Var.apply(ViewState.Overview)
  val viewStateObserver: L.Observer[Unit] = Observer { _ =>
    viewState.set(viewState.now().invert())
  }

  val ticketChildrenRx = Events.ticketsRx.split(
    _.id
  )((id, ticket, ticketSignal) =>
    TicketInnerView(
      ticket,
      ticketSignal.map: t =>
        Ticket(id, t.title, t.description)
    ).ticketPanel(viewState)
  )

  def render() =
    div(
      h1("Ticket List"),
      button(
        child.text <-- viewState.signal.map(_.invert().toString()),
        onClick.mapToUnit --> viewStateObserver
      ),
      children <-- ticketChildrenRx,
      onMountCallback(_ => Events.data.emit(DataEvent.FetchTickets))
    )

