package exp.api

import kyo.*
import exp.model.TodoItem
import scala.concurrent.Future

trait Api[F[_]]:

  def getTodos(): F[IndexedSeq[TodoItem]]

