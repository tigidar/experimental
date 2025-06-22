package exp.events

import exp.model.TodoItem
import kyo.*
import com.raquo.laminar.api.L

enum PageEvent:
  case Home
  case Todo


enum DataEvent:
  case FetchTodos
  case Todos(todos: IndexedSeq[TodoItem])
  case Error(message: String)
  case Empty

object Events:

  /*
  val getTodos: L.Var[IndexedSeq[TodoItem]] =
    L.Var(IndexedSeq[String]("Todo 1", "Todo 2", "Todo 3").zipWithIndex.map {
      (todo: String, index: Int) =>
        TodoItem(s"todo-$index", todo, s"This is a description for $todo")
    })
   */

  val page = new L.EventBus[PageEvent]()
  val pageRx = page.events.toSignal(PageEvent.Todo)

  val data = new L.EventBus[DataEvent]()

  val dataRx: L.Signal[DataEvent] =
    data.events.toSignal(DataEvent.Empty)

  val todosRx: L.Signal[IndexedSeq[TodoItem]] =
    data.events.tapEach( d =>
      println(d)
    ).scanLeft(IndexedSeq.empty[TodoItem]) { (acc, event) =>
      event match
        case DataEvent.Todos(todos) => 
          println("something happens here ?")
          todos
        case _                      => acc
    }

