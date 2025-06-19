package exp.events

import kyo.*

import com.raquo.laminar.api.L

enum PageEvent:
  case Home
  case Todo

final case class TodoItem(id: String, title: String, description: String)

object Events:

  /*
  val getTodos: L.Var[IndexedSeq[TodoItem]] =
    L.Var(IndexedSeq[String]("Todo 1", "Todo 2", "Todo 3").zipWithIndex.map {
      (todo: String, index: Int) =>
        TodoItem(s"todo-$index", todo, s"This is a description for $todo")
    })
  */

  val page = new L.EventBus[PageEvent]()

  val todos: L.EventBus[IndexedSeq[TodoItem]] =
    new L.EventBus[IndexedSeq[TodoItem]]()


