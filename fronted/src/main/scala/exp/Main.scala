package exp

import kyo.*
import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

import exp.backend.{Api, DeveloperApi}
import exp.view.components.{Menu, Page}
import exp.events.{Events, DataEvent, PageEvent}

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

def dataObserver(api: Api): L.Observer[DataEvent] = Observer {
  case DataEvent.Todos(todos) =>
    println(s"Fetched todos: ${todos.map(_.title).mkString(", ")}")
  case DataEvent.Error(message) =>
    println(s"Error fetching todos: $message")
  case DataEvent.Empty =>
    println("No todos available.")
  case DataEvent.FetchTodos =>
    println("Fetching todos...")
    val todos = api.getTodos()
    Events.data.emit(DataEvent.Todos(todos))
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
