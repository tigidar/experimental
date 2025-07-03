package exp

import sttp.tapir.*
import sttp.tapir.server.ServerEndpoint

import exp.Ticket
import sttp.tapir.*
import sttp.tapir.json.upickle.*
import upickle.default.*
import sttp.tapir.generic.auto.*

object TicketContextBoundary:

  def createTicket(
    
  ): Endpoint[Unit, Unit, String, Ticket, Any] =
    endpoint
      .post
      .in("tickets")
      .in(jsonBody[String])
      .out(jsonBody[Ticket])
      .errorOut(stringBody)

    ???

