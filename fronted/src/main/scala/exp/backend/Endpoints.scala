package exp.backend

import sttp.tapir.*
import sttp.tapir.server.ServerEndpoint

val todos: Endpoint[Unit, Unit, Unit, Unit, Any] =
  endpoint.get
    .in("todos")
    //.out(jsonBody[Seq[String]])
