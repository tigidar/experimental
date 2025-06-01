package exp.backend

trait Api:

  def getTodos(): Seq[String]
