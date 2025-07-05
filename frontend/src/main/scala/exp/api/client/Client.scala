package exp.client

import sttp.client4.*
import sttp.tapir.*
import sttp.tapir.client.sttp4.SttpClientInterpreter

//import upickle.default.*
//import sttp.tapir.generic.auto.*

import exp.model.Ticket
import exp.api.Endpoints

object Client:

  val baseUri = uri"http://localhost:8080" // your real or stub base
  val interpreter = SttpClientInterpreter()

  def getTickets() =
    interpreter.toRequest(Endpoints.getTickets(), Some(baseUri)).apply(())

