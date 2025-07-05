package exp.api

import kyo.*
import sttp.client4.{Response}
import exp.domain.Ticket
import scala.concurrent.Future
import exp.client.Client
import exp.backend.Stub
import sttp.tapir.DecodeResult
import sttp.tapir.DecodeResult.Missing
import sttp.client4.Backend
import exp.events.Events
import exp.events.DataEvent
import scala.concurrent.ExecutionContext 

final case class ServerApi(backend: Backend[Future]):

  def getTickets(using exec: ExecutionContext): Future[IndexedSeq[Ticket]] =
    Client.getTickets().send(backend).flatMap(RequestDecoder.apply)

