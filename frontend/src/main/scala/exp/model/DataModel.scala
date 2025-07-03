package exp.model

import upickle.default.{ReadWriter, macroRW}

final case class Ticket(id: String, title: String, description: String)

object Ticket {
  // given rw: ReadWriter[Ticket] = macroRW[Ticket]
}
