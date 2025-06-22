package exp.api

import kyo.*
import exp.model.TodoItem

trait Api:

  def getTodos(): IndexedSeq[TodoItem]
