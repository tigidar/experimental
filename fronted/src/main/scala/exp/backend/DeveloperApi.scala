package exp.backend

import kyo.*
import exp.events.TodoItem

object DeveloperApi:

  val backendApiLayer: Layer[Api, IO] = Layer {
    new Api:
      def getTodos(): IndexedSeq[TodoItem] =
        IndexedSeq("Todo 1", "Todo 2", "Todo 3").zipWithIndex.map {
          (todo: String, index: Int) =>
            TodoItem(s"todo-$index", todo, s"This is a description for $todo")
        }
  }
