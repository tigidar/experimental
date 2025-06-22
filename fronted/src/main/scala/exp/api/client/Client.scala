package exp.client

import sttp.client4.*
import sttp.tapir.*
import sttp.tapir.client.sttp4.SttpClientInterpreter

import sttp.tapir.json.upickle.*
import upickle.default.*
import sttp.tapir.generic.auto.*

import exp.model.TodoItem
import exp.api.Endpoints

object Client:

  val baseUri = uri"http://localhost:8080" // your real or stub base
  val interpreter = SttpClientInterpreter()

  // Construct the request
  val request =
    interpreter.toRequest(Endpoints.fetchTodos, Some(baseUri)).apply(())

