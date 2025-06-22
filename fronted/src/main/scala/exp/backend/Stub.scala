package exp.backend

import sttp.client4.*

import sttp.client4.testing.{BackendStub, SyncBackendStub}
import sttp.tapir.server.stub4.*
import exp.api.Endpoints
import exp.model.TodoItem
import exp.client.Client
import sttp.tapir.server.stub4.TapirStubInterpreter
import scala.concurrent.Future

object Stub:

  given executionContext: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global

  lazy val result = scala.concurrent.Future.successful(
    IndexedSeq(
      TodoItem(
        "todo-1",
        "Todo 1",
        "This is a description for Todo 1"
      ),
      TodoItem(
        "todo-2",
        "Todo 2",
        "This is a description for Todo 2"
      ),
      TodoItem("todo-3", "Todo 3", "This is a description for Todo 3")
    )
  )

  lazy val backend: Backend[Future] =
    TapirStubInterpreter(BackendStub.asynchronousFuture)
      .whenServerEndpoint(
        Endpoints.fetchTodos.serverLogicSuccess { _ =>
          result
        }
      )
      .thenRunLogic()
      .backend()
