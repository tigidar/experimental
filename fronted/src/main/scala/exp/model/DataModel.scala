package exp.model

import upickle.default.{ReadWriter, macroRW}

final case class TodoItem(id: String, title: String, description: String)

object TodoItem {
  given rw: ReadWriter[TodoItem] = macroRW[TodoItem]
}

