package exp.view

import exp.api.Api
import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.{*, given}
import exp.events.{Events, DataEvent}
import exp.model.TodoItem

def todoItemComponent(todo: L.Signal[TodoItem]): HtmlElement =
  div(
    width := "100%",
    height := "50px",
    child <-- todo.map(t => h3(t.title)),
    child <-- todo.map(t => p(t.description))
  )

final case class TodoPage():

  val todoChildRx = Events.todosRx.split(
    _.id
  ) { (id, todo, todoSignal) =>
    todoItemComponent(
      todoSignal.map(t => TodoItem(id, t.title, t.description))
    )
  }

  def render() =
    div(h1("Todo List"), children <-- todoChildRx,
      onMountCallback(_ => Events.data.emit(DataEvent.FetchTodos)),
    )

