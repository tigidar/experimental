package exp.events

import exp.model.Ticket
import kyo.*
import com.raquo.laminar.api.L

enum PageEvent:
  case Home
  case TicketList
  case TicketEdit(ticket: Ticket)

enum DataEvent:
  case FetchTickets
  case Tickets(todos: IndexedSeq[Ticket])
  case Error(message: String)
  case Empty

object Events:

  val page = new L.EventBus[PageEvent]()
  val pageRx = page.events.toSignal(PageEvent.TicketList)

  val data = new L.EventBus[DataEvent]()

  val dataRx: L.Signal[DataEvent] =
    data.events.toSignal(DataEvent.Empty)

  val ticketsOnly: (IndexedSeq[Ticket], DataEvent) => IndexedSeq[Ticket] = {
    (acc, event) =>
      event match
        case DataEvent.Tickets(tickets) => tickets
        case _ => acc
  }

  val ticketsRx: L.Signal[IndexedSeq[Ticket]] =
    data.events.scanLeft(IndexedSeq.empty[Ticket])(ticketsOnly)

