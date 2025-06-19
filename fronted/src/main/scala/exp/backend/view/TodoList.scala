package exp.view

import exp.backend.Api
import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.{*, given}

final case class TodoItem(id: String, title: String, description: String)

val getTodos: L.Var[IndexedSeq[TodoItem]] =
  L.Var(IndexedSeq[String]("Todo 1", "Todo 2", "Todo 3").zipWithIndex.map {
    (todo: String, index: Int) =>
      TodoItem(s"todo-$index", todo, s"This is a description for $todo")
  })

def todoItemComponent(todo: L.Signal[TodoItem]): HtmlElement =
  div(
    width := "100%",
    height := "50px",
    child <-- todo.map(t => h3(t.title)),
    child <-- todo.map(t => p(t.description))
  )

final case class TodoPage(api: Api):

  def render() =
    div(
      h1("Todo List"),
      children <-- getTodos.signal.split(
        _.id
      ) { (id, todo, todoSignal) =>
        todoItemComponent(
          todoSignal.map(t => TodoItem(id, t.title, t.description))
        )
      }
    )

