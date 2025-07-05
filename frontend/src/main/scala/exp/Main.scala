package exp

import kyo.*
import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import sttp.client4.{Response}
import exp.api.{Api, ServerApi}
import exp.view.components.{Menu, Page}
import exp.events.{Events, DataEvent, PageEvent}
import exp.client.Client
import exp.backend.Stub
import exp.domain.Ticket
import scala.concurrent.Future
import sttp.tapir.DecodeResult
import sttp.tapir.DecodeResult.Missing
import exp.client.ClientBackend
import exp.client.StubBackend

given executionContext: scala.concurrent.ExecutionContext =
   scala.concurrent.ExecutionContext.global

extension [A](fa: Future[A])
  def emit[B](f: A => DataEvent): Unit =
    fa.map(f).foreach{ d => Events.data.emit(d)}

lazy val appContainer =
  dom.document.getElementById(
    "app"
  )

def pageEventObserver(api: ServerApi): L.Observer[PageEvent] = Observer {
  // event => exp.events.Events.page.writer.onNext(event)
  case PageEvent.TicketList =>
    // TODO: Fetch todos from the API and update the state
    println("todo")
  case _ =>
}

def decodeOrFail[E, O](res: Response[DecodeResult[Either[E, O]]]): Future[O] =
  res.body match {
    case DecodeResult.Value(Right(value)) => Future.successful(value)
    case DecodeResult.Value(Left(error)) =>
      Future.failed(new Exception(s"Error: $error"))
    case _ =>
      Future.failed(
        new Exception("Unexpected response format")
      )
  }

def dataObserver(api: ServerApi): L.Observer[DataEvent] = Observer {
  case DataEvent.Tickets(todos) =>
  // println(s"Fetched todos: ${todos.map(_.title).mkString(", ")}")
  case DataEvent.Error(message) =>
  // println(s"Error fetching todos: $message")
  case DataEvent.Empty =>
  // println("No todos available.")
  case DataEvent.FetchTickets =>
    api.getTickets.emit(DataEvent.Tickets.apply)
}

def renderDom(): Unit < (Env[ClientBackend[Future]] & IO) =
  for
    clientBackend <- Env.get[ClientBackend[Future]]
    api = ServerApi(clientBackend.get())
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

  val editPage = PageEvent.TicketEdit(exp.backend.generator.ticketFactory())

  val listPage = PageEvent.TicketList

  def startApp: Unit < IO =
    Memo.run(Env.runLayer(StubBackend.backendLayer)(renderDom()))

  run {
    for _ <- startApp
    _ = println("App started")
    _ <- IO(Events.page.emit(listPage))
    yield ()
  }

}
