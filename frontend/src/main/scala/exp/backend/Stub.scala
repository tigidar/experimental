package exp.backend

import sttp.client4.*

import sttp.client4.testing.{BackendStub, SyncBackendStub}
import sttp.tapir.server.stub4.*
import exp.api.TicketEndpoints
import exp.client.Client
import sttp.tapir.server.stub4.TapirStubInterpreter
import scala.concurrent.Future
import exp.api.TicketEndpoints
import scala.concurrent.ExecutionContext

import sttp.tapir.json.pickler.*
import sttp.tapir.json.pickler.generic.auto.*
import sttp.tapir.generic.auto.*

final class Stub(using exec: ExecutionContext):

  lazy val result = scala.concurrent.Future.successful(
    IndexedSeq(exp.backend.generator.ticketFactory()
  ))

  lazy val backend: Backend[Future] =
    TapirStubInterpreter(BackendStub.asynchronousFuture)
      .whenServerEndpoint(
        TicketEndpoints.getTickets().serverLogicSuccess { _ =>
          result
        }
      )
      .thenRunLogic()
      .backend()

