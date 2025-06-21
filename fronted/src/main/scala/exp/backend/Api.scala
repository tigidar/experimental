package exp.backend

import kyo.*
import exp.events.TodoItem

trait Api:

  def getTodos(): IndexedSeq[TodoItem]
