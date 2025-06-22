package exp.api

import kyo.*
import sttp.client4.{Response}
import exp.model.TodoItem
import scala.concurrent.Future
import exp.client.Client
import exp.backend.Stub
import sttp.tapir.DecodeResult
import sttp.tapir.DecodeResult.Missing
import sttp.client4.Backend
import exp.events.Events
import exp.events.DataEvent

given executionContext: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global

final case class ServerApi(backend: Backend[Future]):

  def getTodos(): Future[IndexedSeq[TodoItem]] =
    Client.request.send(backend).flatMap(RequestDecoder.apply)

