package exp.client

import sttp.client4.*
import sttp.tapir.*
import sttp.tapir.client.sttp4.SttpClientInterpreter

import exp.domain.Ticket
import exp.api.TicketEndpoints


object Client:

  val baseUri = uri"http://localhost:8080" // your real or stub base
  val interpreter = SttpClientInterpreter()

  inline def getTickets() =
    interpreter.toRequest(TicketEndpoints.getTickets(), Some(baseUri)).apply(())
