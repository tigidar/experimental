package exp.api

import sttp.tapir.*
import sttp.tapir.server.ServerEndpoint

import exp.model.TodoItem
import sttp.tapir.*
import sttp.tapir.json.upickle.*
import upickle.default.*
import sttp.tapir.generic.auto.*

object Endpoints {

  val fetchTodos: Endpoint[Unit, Unit, String, IndexedSeq[TodoItem], Any] =
    endpoint.get
      .in("todos")
      .out(jsonBody[IndexedSeq[TodoItem]])
      .errorOut(stringBody)

}

