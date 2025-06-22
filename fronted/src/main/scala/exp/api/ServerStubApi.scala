package exp.api

import kyo.*
import exp.model.TodoItem
import scala.concurrent.Future
import exp.client.Client
import exp.backend.Stub

object ServerStubApi:

  given executionContext: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global

  val backendApiLayer: Layer[Api[Future], IO] = Layer {
    new Api[Future] {
      
      def getTodos(): Future[IndexedSeq[TodoItem]] =
        Client.request.send(Stub.backend).flatMap(RequestDecoder.apply)

    }
  }
