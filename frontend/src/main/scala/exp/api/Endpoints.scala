package exp.api

import sttp.tapir.*
import sttp.tapir.server.ServerEndpoint

import exp.model.Ticket
import sttp.tapir.*
import sttp.tapir.json.pickler.*

object Endpoints:

  given Pickler[Ticket] = Pickler.derived[Ticket]

  def getTickets() : Endpoint[Unit, Unit, String, IndexedSeq[Ticket], Any] =
    endpoint.get
      .in("tickets")
      .out(jsonBody[IndexedSeq[Ticket]])
      .errorOut(stringBody)

  def updateTickets() : Endpoint[Unit, Unit, String, Ticket, Any] =
    endpoint.get
      .in("tickets")
      .out(jsonBody[Ticket])
      .errorOut(stringBody)


