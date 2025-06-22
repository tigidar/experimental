package exp

import kyo.*
import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import sttp.client4.{Response}
import exp.api.{Api, DeveloperApi}
import exp.view.components.{Menu, Page}
import exp.events.{Events, DataEvent, PageEvent}
import exp.client.Client
import exp.backend.Stub
import exp.model.TodoItem
import scala.concurrent.Future
import sttp.tapir.DecodeResult
import sttp.tapir.DecodeResult.Missing

given executionContext: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global

lazy val appContainer =
  dom.document.getElementById(
    "app"
  )

def pageEventObserver(api: Api): L.Observer[PageEvent] = Observer {
  // event => exp.events.Events.page.writer.onNext(event)
  case PageEvent.Todo =>
    // TODO: Fetch todos from the API and update the state
    println("todo")
  case _ =>
}

def decodeOrFail[E, O](res: Response[DecodeResult[Either[E, O]]]): Future[O] =
  res.body match {
    case DecodeResult.Value(Right(value)) => Future.successful(value)
    case DecodeResult.Value(Left(error))  => Future.failed(new Exception(s"Error: $error"))
    case Missing =>
      Future.failed(new Exception("Missing data in response"))
    //case DecodeResult.Failure(_, _, e)    => Future.failed(e)
  }


def dataObserver(api: Api): L.Observer[DataEvent] = Observer {
  case DataEvent.Todos(todos) =>
    //println(s"Fetched todos: ${todos.map(_.title).mkString(", ")}")
  case DataEvent.Error(message) =>
    //println(s"Error fetching todos: $message")
  case DataEvent.Empty =>
    //println("No todos available.")
  case DataEvent.FetchTodos =>
    val f: Future[IndexedSeq[TodoItem]] = Client.request.send(Stub.backend).flatMap(
      decodeOrFail)

    L.Signal.fromFuture[IndexedSeq[TodoItem]](f).map { response =>
      response match {
        case Some(todos) =>
          println(s"Fetched todos via the backend thingy: ${todos.map(_.title).mkString(", ")}")
          Events.data.emit(DataEvent.Todos(todos.asInstanceOf[IndexedSeq[TodoItem]]))
        case None =>
          Events.data.emit(DataEvent.Error("failed to fetch todos"))
      }
    }.foreach(_ => ())(using unsafeWindowOwner)

}

def renderDom(): Unit < (Env[Api] & IO) =
  for
    api <- Env.get[Api]
    _ <- IO(
      render(
        appContainer,
        div(
          width := "100%",
          height := "100%",
          Events.page.events --> pageEventObserver(api),
          Events.data.events --> dataObserver(api),
          Menu.view,
          Page.view()
        )
      )
    )
  yield ()

object ExperimentalApp extends KyoApp {

  lazy val startApp: Unit < IO =
    Memo.run(Env.runLayer(DeveloperApi.backendApiLayer)(renderDom()))

  run {
    for _ <- startApp
    yield ()
  }

}
