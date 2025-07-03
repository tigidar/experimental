package exp.api

import sttp.tapir.*
import sttp.tapir.server.ServerEndpoint

import exp.model.Ticket
import sttp.tapir.*
import sttp.tapir.json.upickle.*
import upickle.default.*
import sttp.tapir.generic.auto.*

object Endpoints:

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


