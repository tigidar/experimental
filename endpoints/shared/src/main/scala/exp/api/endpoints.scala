package exp.api

import sttp.tapir.*
//import sttp.tapir.json.upickle._

import sttp.tapir.json.pickler.*
import sttp.tapir.server.ServerEndpoint
import exp.domain.Ticket
import exp.codec.{given}

import exp.domain.TicketCategory

object TicketEndpoints:

  def createTicket(
  ): Endpoint[Unit, String, String, Ticket, Any] =
    endpoint.post
      .in("tickets")
      .in(jsonBody[String])
      .out(jsonBody[Ticket])
      .errorOut(stringBody)

  def getTickets(): Endpoint[Unit, Unit, String, IndexedSeq[Ticket], Any] =
    endpoint.get
      .in("tickets")
      .out(jsonBody[IndexedSeq[Ticket]])
      .errorOut(stringBody)

  def updateTickets(): Endpoint[Unit, Unit, String, Ticket, Any] =
    endpoint.get
      .in("tickets")
      .out(jsonBody[Ticket])
      .errorOut(stringBody)

    ???

