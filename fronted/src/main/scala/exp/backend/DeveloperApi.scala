package exp.backend

import kyo.*

object DeveloperApi:

  val backendApiLayer: Layer[Api, IO] = Layer {
    new Api:
      def getTodos(): Seq[String] < IO = IO(Seq("Todo 1", "Todo 2", "Todo 3"))
  }

