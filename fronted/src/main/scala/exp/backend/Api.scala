package exp.backend

import kyo.*

trait Api:

  def getTodos(): Seq[String] < IO
